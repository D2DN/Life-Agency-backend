// This file has the default event types for the application. 
// In order to have it on the databse, the command below will insert the documents written here to the base indicated.
// mongo < event-types.js

use lifeagencyserver-developpement

db.eventType.insert(
    [
        {
            "_id" : "ef5b6596-d392-11e7-9296-cec278b6b50a",
            "_class" : "com.sii.lifeagency.pojo.EventType",
            "name" : "Birthday",
            "banner" : {
                "_id" : "9f1f0c9e-b5f7-4e9f-8f53-ccb958def3e0",
                "url" : "/image/9f1f0c9e-b5f7-4e9f-8f53-ccb958def3e0"
            }
        },
        {
            "_id" : "ef5b6910-d392-11e7-9296-cec278b6b50a",
            "_class" : "com.sii.lifeagency.pojo.EventType",
            "name" : "Certif",
            "banner" : {
                "_id" : "20351453-dfc3-4392-be38-e96347223966",
                "url" : "/image/20351453-dfc3-4392-be38-e96347223966"
            }
        },
        {
            "_id" : "ef5b6b7c-d392-11e7-9296-cec278b6b50a",
            "_class" : "com.sii.lifeagency.pojo.EventType",
            "name" : "Event",
            "banner" : {
                "_id" : "7995300b-5157-4400-a260-ec083fe60241",
                "url" : "/image/7995300b-5157-4400-a260-ec083fe60241"
            }
        },
        {
            "_id" : "ef5b6c8a-d392-11e7-9296-cec278b6b50a",
            "_class" : "com.sii.lifeagency.pojo.EventType",
            "name" : "Newbie",
            "banner" : {
                "_id" : "b63b4b3e-ce89-4339-a85b-85f1411b64fc",
                "url" : "/image/b63b4b3e-ce89-4339-a85b-85f1411b64fc"
            }
        },
        {
            "_id" : "ef5b70c2-d392-11e7-9296-cec278b6b50a",
            "_class" : "com.sii.lifeagency.pojo.EventType",
            "name" : "Party",
            "banner" : {
                "_id" : "d66c1dda-3a70-40a6-9353-5d22f735b231",
                "url" : "/image/d66c1dda-3a70-40a6-9353-5d22f735b231"
            }
        },
        {
            "_id" : "ef5b72ca-d392-11e7-9296-cec278b6b50a",
            "_class" : "com.sii.lifeagency.pojo.EventType",
            "name" : "Win",
            "banner" : {
                "_id" : "f9247b88-a0ad-42f9-b779-5c7dd4fa092b",
                "url" : "/image/f9247b88-a0ad-42f9-b779-5c7dd4fa092b"
            }
        }
    ]
);