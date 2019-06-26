const AWS = require('aws-sdk')

const ddb = new AWS.DynamoDB.DocumentClient()

var word = [
  "강아지", "고양이", "선풍기", "가방", "서랍", "책상", "방향", "영어", "의자", "사진"
]

var game_params = {
  TableName: "Game",
  Item: {
    id: 1,
    nowWordIdx : 0,
    answeredWords: ", ",
    nowConsonant: getConsonant(word[0])
    }
}

exports.handler = async (event, context) => {
  const connectionId = event.requestContext.connectionId
  const eventType = event.requestContext.eventType

  if (eventType === "CONNECT") {
    console.log("Connect Requested")

    let params = {
      TableName: "User",
      Item: {
        connectionId: connectionId,
        answerCnt: 0
      }
    }
    await putToDyDB(params)

    if(await isOne()) {
      game_params.Item.nowWordIdx = 0
      game_params.Item.nowConsonant = getConsonant(word[0])
      game_params.Item.answeredWords = ", "
      await putToDyDB(game_params)
    }
  } else if (eventType === "DISCONNECT") {
    console.log("Disconnect Requested")

    let params = {
      TableName: "User",
      Key: {
        connectionId: connectionId
      }
    }
    
    await deleteFromDyDB(params)
  } else if (eventType === "MESSAGE") {
    let isJson = true
    var payload
    var keys
    
    try {
      payload = JSON.parse(event.body)
      keys = Object.keys(payload)
    }catch(e) {
      isJson = false
    }
      
    if(isJson === true && keys.includes("content") && keys.includes("username")) {
      let params = {
        TableName: "Chat",
        Item: {
          "room": "general",
          "content": payload.content,
          "timestamp": new Date().toISOString(),
          "username": payload.username
        }
      }
      await putToDyDB(params)
      await getGameData().then(async (result) => {
        if(isAnswer(payload.content)) {
          await addScore(connectionId)
          await updateGameData(payload.content)
        }
        await broadcastMsg(event, payload)
      })
    }
    
    await broadcastGameData(event)
  } else {
    return { statusCode: 404, body: "illegal access" }
  }

  return { statusCode: 200, body: 'Data sent.' }
}

async function putToDyDB(params) {
  await ddb.put(params, function(err, data) {
    if (err) {
      console.error("Unable to add item to '" + params.TableName + "' Table. Error JSON:", JSON.stringify(err, null, 2))
    } else {
      console.log("Added item to '" + params.TableName + "' Table:", JSON.stringify(data, null, 2))
    }
  }).promise()
}

async function deleteFromDyDB(params) {
  await ddb.delete(params, function(err, data) {
    if (err) {
      console.error("Unable to delete item from '" + params.TableName + "' Table. Error JSON:", JSON.stringify(err, null, 2))
    } else {
      console.log("Deleted item from '" + params.TableName + "' Table:", JSON.stringify(data, null, 2))
    }
  }).promise()
}

async function getFromDyDB(params) {
  let result = null
  await ddb.get(params, function(err, data) {
    if (err) {
      console.error("Unable to get item from '" + params.TableName + "' Table. Error JSON:", JSON.stringify(err, null, 2))
    } else {
      console.log("Got item from '" + params.TableName + "' Table:", JSON.stringify(data, null, 2))
      result = data
    }
  }).promise()
  return result
}

async function getGameData() {
  var params = {
    TableName: "Game",
    Key: {
      id: 1
    }
  }

  await getFromDyDB(params).then((result) => {
    game_params.Item.nowWordIdx = result.Item.nowWordIdx
    game_params.Item.answeredWords = result.Item.answeredWords
  })
}

async function isOne() {
  try {
    connectionData = await ddb.scan({ TableName: "User", ProjectionExpression: 'connectionId' }).promise()
  } catch (e) {
    return { statusCode: 500, body: e.stack }
  }

  if(connectionData.Count == 1) {
    return true
  } else {
    return false
  }
}

async function addScore(connectionId) {
  let params = {
    TableName: "User",
    Key: {
      connectionId: connectionId
    }
  }

  await getFromDyDB(params).then(async (result) => {
    const newAnswerCnt = result.Item.answerCnt + 1
    let params = {
      TableName: "User",
      Item: {
        connectionId: connectionId,
        answerCnt: newAnswerCnt
      }
    }
    await putToDyDB(params)
  })
}

function isAnswer(content) {
  if(game_params.Item.answeredWords.includes(content) == true) {
    return false
  }
  if(word[game_params.Item.nowWordIdx] !== content) {
    return false
  }
  return true
}

async function updateGameData(content) {
  console.log(game_params)
  game_params.Item.answeredWords += (", " + content)
  game_params.Item.nowWordIdx++
  game_params.Item.nowWordIdx %= word.length
  game_params.Item.nowConsonant = getConsonant(word[game_params.Item.nowWordIdx])

  if(game_params.Item.nowWordIdx == 0) {
    game_params.Item.answeredWords = ""
  }

  await putToDyDB(game_params)
}

async function broadcastGameData(event) {
  try {
    connectionData = await ddb.scan({ TableName: "User", ProjectionExpression: 'connectionId' }).promise()
  } catch (e) {
    return { statusCode: 500, body: e.stack }
  }
    
  const apigwManagementApi = new AWS.ApiGatewayManagementApi({
    endpoint: event.requestContext.domainName + '/' + event.requestContext.stage
  })
    
  const postCalls = connectionData.Items.map(async ({ connectionId }) => {
    try {
      await apigwManagementApi.postToConnection({ ConnectionId: connectionId, Data: JSON.stringify(game_params.Item) }).promise()
    } catch (e) {
      return { statusCode: 500, body: e.stack }
    }
  })

  try {
    await Promise.all(postCalls)
  } catch (e) {
    return { statusCode: 500, body: e.stack }
  }
}


async function broadcastMsg(event, payload) {
  try {
    connectionData = await ddb.scan({ TableName: "User", ProjectionExpression: 'connectionId' }).promise()
  } catch (e) {
    return { statusCode: 500, body: e.stack }
  }
    
  const apigwManagementApi = new AWS.ApiGatewayManagementApi({
    endpoint: event.requestContext.domainName + '/' + event.requestContext.stage
  })
    
  const postCalls = connectionData.Items.map(async ({ connectionId }) => {
    try {
      await apigwManagementApi.postToConnection({ ConnectionId: connectionId, Data: JSON.stringify(payload) }).promise()
    } catch (e) {
      return { statusCode: 500, body: e.stack }
    }
  })

  try {
    await Promise.all(postCalls)
  } catch (e) {
    return { statusCode: 500, body: e.stack }
  }
}

function getConsonant(str) {
  let consonant = ["ㄱ","ㄲ","ㄴ","ㄷ","ㄸ","ㄹ","ㅁ","ㅂ","ㅃ","ㅅ","ㅆ","ㅇ","ㅈ","ㅉ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"]
  let result = ""
  for(let i = 0 ; i < str.length ; i++) {
    let code = str.charCodeAt(i) - 44032
    if(code > -1 && code < 11172) {a
        result += consonant[Math.floor(code/588)]
    }
  }
  return result
}