# API Documentation

Takes all initial information required and create a new account

## Base URL

The base URL for all API requests is: [`http://localhost:8000`](http://localhost:8000/)

## Authentication

To access the API, you need to include your API key in the `Authorization` header of your requests.

- **Header**: `Authorization: Bearer YOUR_API_KEY`

## Parameters

- **`{id}`** (path parameter): ID of the item.

## Endpoints

### SingUp

### **Update User**

- **URL**: **`/users/:userId`**
- **Method**: **`PUT`**
- **Parameters**:
    - **`userId`** (string, required): The ID of the user to be updated.
    - **Request Body**:
        - **`firstname`** (string): The updated first name of the user. It can be between 1 and 256 characters long.
        - **`lastname`** (string): The updated last name of the user. It can be between 1 and 256 characters long.
        - **`country`** (string): The updated country of residence for the user. It can be between 1 and 100 characters long.
        - **`city`** (string): The updated city of residence for the user. It can be between 1 and 100 characters long.
        - **`bio`** (string): The updated biography of the user. It can be up to 1000 characters long.
        - **`friends`** (array of strings): The updated list of friends for the user.

### Example Request

```jsx
{
    "firstname":"marwa",
    "lastname":"ahmed",
    "bio":"hi hiiiiiiiiiiiiiii",
    "selectedBadges":["65b6c3276c0280cad1437826","65b6fbc406914b956cce88aa","65b6ff2b367b7524af737ca0"]
}
```

### Example Response

```jsx
{
    "status": "success",
    "message": "Registration successful",
    "body": "No data exist!"
}
```

### Error Handling:

```markdown
**## Error Handling**

- **400 Bad Request**: If any of the provided fields fail validation, or if the user is attempting to update another user's information.
- **404 Not Found**: If the user to be updated is not found.
- **500 Internal Server Error**: If there is an issue with the server processing the request.
```