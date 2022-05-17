import json
import boto3
import base64

s3 = boto3.client('s3')

def lambda_handler(event, context):
    # TODO implement
    if event['httpMethod'] == "POST":
        data = json.loads(event['body'])
        name = data['name']
        name="Images/" + name
        
        imageb64 = data['file']
        image = base64.b64decode(imageb64)
        s3.put_object(
            Bucket='images-detection',
            Key=name,
            Body=image,
            ACL='public-read',
            ContentType='mimetype',
            ContentDisposition='inline'
            )
    
    return {
        'statusCode': 200,
        'body': json.dumps({'s3-url': name}),
        'headers': {'Access-Control-Allow-Origin': '*'}
    }
