# Coding Standards for Amazon Q

## Java Code Style
- Follow Google Java Style Guide conventions
- Use camelCase for variable and method names
- Use PascalCase for class names
- Use UPPER_SNAKE_CASE for constants
- Maximum line length: 100 characters

## Code Quality
- Methods should not exceed 30 lines of code
- Classes should have a single responsibility
- Avoid nested conditionals deeper than 3 levels
- Write unit tests for all public methods
- Document all public APIs with Javadoc

## Performance Considerations
- Prefer StringBuilder over String concatenation in loops
- Use appropriate collection types for the use case
- Close resources in try-with-resources blocks
- Avoid premature optimization
- Consider thread safety in shared objects