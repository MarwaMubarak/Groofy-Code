# API Documentation

Takes all initial information required and create a new account

## Base URL

The base URL for all API requests is: [`http://localhost:8000`](http://localhost:8000/)

## Authentication

To access the API, you need to include your API key in the `Authorization` header of your requests.

- **Header**: `Authorization: Bearer YOUR_API_KEY`

## Endpoints

### SingUp

### **Signup**

### **Change Password**

- **URL**: **`/change-password`**
- **Method**: **`PUT`**
- **Parameters**:
  - **Request Body**:
    - **`currentPassword`** (string, required): The current password of the user.
    - **`password`** (string, required): The new password for the user. It must pass the complexity requirements.
    - **`confirmPassword`** (string, required): The confirmation of the new password. Must match the **`password`** field.

### Example Request

```jsx
{
    "currentPassword":"12345@Gc$",
    "password":"12345@Gc",
    "confirmPassword":"12345@Gc"

}
```

### Example Response

```jsx
{
    "status": "success",
    "message": "Password Updated Successfully!",
    "body": {
        "city": "",
        "bio": "",
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
        "updatedAt": "2024-01-31T21:42:19.502Z",
        "Trophies": 0
    }
}
```

### Error Handling:

```markdown
**## Error Handling**

- **400 Bad Request**:
  - If the current password is invalid.
  - If the new password fails validation or doesn't match the confirmation.
- **404 Not Found**: If the user is not found.
- **500 Internal Server Error**: If there is an issue with the server processing the request.
```
