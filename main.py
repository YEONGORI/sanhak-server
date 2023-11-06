import json
import pymongo

def lambda_handler(event, context):
    mongo_client = pymongo.MongoClient("mongodb+srv://yeongori:yeongori@cluster0.q3wl7qq.mongodb.net/?retryWrites=true&w=majority")
    mongo_database = mongo_client['test']
    mongo_collection = mongo_database['cad']
    
    data = mongo_collection.find()
    data = data[:10]
    data = list(data)

    return {
        'statusCode': 200,
        'body': json.dumps(data, default=str, ensure_ascii=False)
    }
