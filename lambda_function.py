import json
import boto3
import object_detection
import base64
from datetime import datetime,timezone
import time
import os

s3_client= boto3.client('s3')
TABLE_NAME = 'Image'
dynamodb = boto3.resource('dynamodb')

os.putenv('TZ','Australia/Melbourne')
time.tzset()

def lambda_handler(event, context):
    # TODO implement
    date = datetime.now(timezone.utc).astimezone().strftime('%Y-%m-%d')
    print(date)
    for record in event["Records"]:
        bucket = record['s3']['bucket']['name']
        key = record['s3']['object']['key']
        print("File {0} uploaded to {1} bucket".format(key,bucket))
        image=s3_client.get_object(Bucket=bucket, Key=key)
        print(image)
        image_data=base64.b64encode(image['Body'].read()).decode('utf-8')
        
        tags=object_detection.image_detection(image_data)
        s3_url="s3://{0}/{1}".format(bucket,key)
        
        table=dynamodb.Table(TABLE_NAME)
        response=table.put_item(
            Item={
                'Date':date,
                's3-url':s3_url,
                'tags':tags
            }
            )
            
    return {
        'statusCode': 200,
        'body': json.dumps('Image detected')
    }


