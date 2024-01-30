# API Documentation

Takes all initial information required and create a new account

## Base URL

The base URL for all API requests is: [`http://localhost:8000`](http://localhost:8000/)

## Authentication

To access the API, you need to include your API key in the `Authorization` header of your requests.

- **Header**: `Authorization: Bearer YOUR_API_KEY`

## Endpoints

### search

- **URL**: **`/users/search/:prefix`**
- **Method**: **`GET`**
- **Parameters**:
    - **`prefix`** (string, required): The prefix to search for in usernames.

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
    "message": "Usernames found",
    "body": [
        "esawy"
    ]
}
```

### Error Handling:

```markdown
**## Error Handling**

- **400 Bad Request**: enter prefix.
- **400 not found**: no user match the search.
- **500 server error**: the backend crash contact us.
```