# Coding Standards for Amazon Q

## Java Code Style
- Follow Google Java Style Guide conventions
- Use camelCase for variable and method names
- Use PascalCase for class names
- Use UPPER_SNAKE_CASE for constants
- Maximum line length: 100 characters

## Error Handling
- Never return null for collections - use empty collections instead
- Always include specific exception types in catch blocks rather than generic Exception
- Log exceptions with contextual information before rethrowing or handling
- Use try-with-resources for all AutoCloseable resources
- Include meaningful error messages that help with troubleshooting

## Code Quality
- Methods should not exceed 30 lines of code
- Classes should have a single responsibility
- Avoid nested conditionals deeper than 3 levels
- Write unit tests for all public methods
- Document all public APIs with Javadoc