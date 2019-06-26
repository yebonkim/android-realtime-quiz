const AWS = require('aws-sdk')

const ddb = new AWS.DynamoDB.DocumentClient()

exports.handler = async (event, context) => {
  let connectionData
  
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

  } else if (eventType === "DISCONNECT") {
    console.log("Disconnect Requested")

    let params = {
        TableName: "User",
        Key: {
            connectionId: connectionId
        }
    }
    
    await deleteFromDyDB(params)
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