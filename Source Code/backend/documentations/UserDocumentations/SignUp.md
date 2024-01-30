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

- **URL**: **`/signup`**
- **Method**: **`POST`**
- **Parameters**:
    - **`username`** (string, required): The desired username for the new account. It must be between 1 and 100 characters long.
    - **`email`** (string, required): The email address for the new account. It must be a valid email between 4 and 256 characters long.
    - **`password`** (string, required): The password for the new account. It must pass the complexity requirements.
    - **`firstname`** (string): The first name of the user. It can be between 1 and 256 characters long.
    - **`lastname`** (string): The last name of the user. It can be between 1 and 256 characters long.
    - **`country`** (string): The country of residence for the user. It can be between 1 and 100 characters long.

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