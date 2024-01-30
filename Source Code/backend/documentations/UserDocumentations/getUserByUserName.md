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

### **Get User By Username**

- **URL**: **`/users/:username`**
- **Method**: **`GET`**
- **Parameters**:
    - **`username`** (string, required): The username of the user to retrieve.

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

- **400 Bad Request**: email or user name already exist.
- **500 server error**: the backend crash contact us.
```