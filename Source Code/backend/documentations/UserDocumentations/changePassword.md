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
    "username":"mohamed",
    "email":"moahmed@gmail.com",
    "password":"12345@Gc",
    "firstname":"ali",
    "lastname":"Mohamed",
    "country": "Egypt"
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

- **400 Bad Request**:
  - If the current password is invalid.
  - If the new password fails validation or doesn't match the confirmation.
- **404 Not Found**: If the user is not found.
- **500 Internal Server Error**: If there is an issue with the server processing the request.
```
