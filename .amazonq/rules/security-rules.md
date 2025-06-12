# Security Rules for Amazon Q

## Security Best Practices
- Always use parameterized queries to prevent SQL injection
- Never store credentials in code
- Use proper authentication and authorization mechanisms
- Validate all user inputs
- Apply the principle of least privilege

## Java-Specific Security Rules
- Use PreparedStatement for database queries
- Implement proper exception handling with specific catch blocks
- Avoid serialization of sensitive data
- Use secure random number generators for security operations
- Apply input validation on all public methods