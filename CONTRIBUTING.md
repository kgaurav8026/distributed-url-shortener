# Contributing to Distributed URL Shortener

First off, thank you for considering contributing to Distributed URL Shortener! It's people like you that make this project such a great tool.

## Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code. Please report unacceptable behavior to the project maintainers.

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the existing issues as you might find out that you don't need to create one. When you are creating a bug report, please include as many details as possible:

* **Use a clear and descriptive title**
* **Describe the exact steps to reproduce the problem**
* **Provide specific examples**
* **Describe the behavior you observed and what you expected to see**
* **Include screenshots if relevant**
* **Include your environment details** (OS, Java version, etc.)

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, please include:

* **Use a clear and descriptive title**
* **Provide a detailed description of the suggested enhancement**
* **Provide specific examples to demonstrate the steps**
* **Describe the current behavior and explain the expected behavior**
* **Explain why this enhancement would be useful**

### Pull Requests

* Fork the repository and create your branch from `main`
* If you've added code that should be tested, add tests
* Ensure the test suite passes
* Make sure your code follows the existing code style
* Write a clear commit message
* Include relevant issue numbers in your PR description

## Development Setup

1. **Fork and clone the repo**
   ```bash
   git clone https://github.com/yourusername/distributed-url-shortner.git
   cd distributed-url-shortner
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Start Docker services**
   ```bash
   docker-compose up -d
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Run tests**
   ```bash
   mvn test
   ```

## Coding Standards

### Java Code Style

* Follow standard Java conventions
* Use meaningful variable and method names
* Add JavaDoc comments for public methods
* Keep methods small and focused
* Use Lombok annotations to reduce boilerplate

### Commit Messages

* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
* Limit the first line to 72 characters or less
* Reference issues and pull requests liberally

Examples:
```
Add URL expiration notification feature
Fix Redis connection timeout issue #123
Update README with new configuration options
```

### Testing

* Write unit tests for new features
* Ensure all tests pass before submitting PR
* Aim for high code coverage
* Include integration tests where appropriate

## Project Structure

```
src/
├── main/
│   ├── java/org/example/
│   │   ├── config/         # Configuration classes
│   │   ├── controller/     # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── exception/      # Exception handling
│   │   ├── repository/     # Data access layer
│   │   ├── service/        # Business logic
│   │   └── util/           # Utility classes
│   └── resources/
│       ├── static/         # Frontend assets
│       └── application.properties
└── test/
    └── java/               # Test classes
```

## Questions?

Feel free to open an issue with your question or contact the maintainers directly.

Thank you for contributing! 🎉

