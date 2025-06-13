# PR Review Rules for Amazon Q

## Java Code Review Rules
- Verify code follows Google Java Style Guide conventions
- Check that camelCase is used for variable and method names
- Ensure PascalCase is used for class names
- Confirm UPPER_SNAKE_CASE is used for constants
- Validate maximum line length does not exceed 100 characters

## Error Handling Review
- Verify collections return empty collections instead of null
- Check that specific exception types are used in catch blocks
- Ensure exceptions are logged with contextual information
- Confirm try-with-resources is used for all AutoCloseable resources
- Validate error messages are meaningful and help with troubleshooting

## Code Quality Review
- Check that methods do not exceed 30 lines of code
- Verify classes have a single responsibility
- Ensure nested conditionals are not deeper than 3 levels
- Confirm unit tests exist for all public methods
- Validate all public APIs have Javadoc documentation

## Security Review
- Verify parameterized queries are used to prevent SQL injection
- Check that no credentials are stored in code
- Ensure proper authentication and authorization mechanisms are used
- Confirm all user inputs are validated
- Validate the principle of least privilege is applied