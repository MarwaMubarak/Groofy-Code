# API Documentation

Authenticate a user and retrieve their attributes and its token.


## Base URL

The base URL for all API requests is: [`http://localhost:8000`](http://localhost:8000/)

## Authentication

To access the API, you need to include your API key in the `Authorization` header of your requests.

- **Header**: `Authorization: Bearer YOUR_API_KEY`

## Parameters

- **`{id}`** (path parameter): ID of the item.

## Endpoints

### 1. login

- **URL**: `/login`
- **Method**: `POST`

### Example Request

```jsx
{
    "emailOrUserName":"esawy@gmail.com",
    "password":"12345@Gc"
}
```

### Example Response

```jsx
{
    "status": "success",
    "message": "login successful",
    "body": {
        "_id": "65b7e209015b539b257e0f0b",
        "username": "esawy",
        "email": "esawy@gmail.com",
        "firstname": "Omar",
        "lastname": "Esawy",
        "country": "Egypt",
        "badges": [],
        "selectedBadges": [],
        "friends": [],
        "photo": {
            "url": "https://images.are.na/eyJidWNrZXQiOiJhcmVuYV9pbWFnZXMiLCJrZXkiOiI4MDQwOTc0L29yaWdpbmFsX2ZmNGYxZjQzZDdiNzJjYzMxZDJlYjViMDgyN2ZmMWFjLnBuZyIsImVkaXRzIjp7InJlc2l6ZSI6eyJ3aWR0aCI6MTIwMCwiaGVpZ2h0IjoxMjAwLCJmaXQiOiJpbnNpZGUiLCJ3aXRob3V0RW5sYXJnZW1lbnQiOnRydWV9LCJ3ZWJwIjp7InF1YWxpdHkiOjkwfSwianBlZyI6eyJxdWFsaXR5Ijo5MH0sInJvdGF0ZSI6bnVsbH19?bc=0",
            "publicID": null
        },
        "status": false,
        "isOnline": true,
        "totalMatch": 0,
        "highestTrophies": 0,
        "wins": 0,
        "loses": 0,
        "draws": 0,
        "division": "",
        "createdAt": "2024-01-29T17:36:09.772Z",
        "updatedAt": "2024-01-30T08:06:00.533Z",
        "Trophies": 0,
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1YjdlMjA5MDE1YjUzOWIyNTdlMGYwYiIsImlhdCI6MTcwNjYwNjQwMSwiZXhwIjoxNzA5MTk4NDAxfQ.PZmxUvVXUGtOr2F9J9cLGG8NqlDUY-__1gcZTA2A5q4"
    }
}
```

### Error Handling:

```markdown
**## Error Handling**

- **400 Bad Request**: wrong password or email/username.
- **500 Not Found**: the backend crash contact us.
```