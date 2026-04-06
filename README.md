# DS-Monitoring Add-on

A CDI-based method invocation monitoring add-on that records timing and exception
data for annotated methods. Originally built on Apache DeltaSpike, now migrated to
standard Jakarta EE 11 APIs.

## Architecture

The project is split into two modules:

- **core** - Provides the `@InvocationMonitored` interceptor binding, the
  `InvocationMonitoredInterceptor`, a pluggable `InvocationMonitoredStrategy` SPI,
  and a request-scoped `MonitoredMethodInvocationStorage`.
- **jsf** - Adds JSF view-ID awareness by specializing the core storage and
  wrapping invocation descriptors with the active view ID.

## Usage

1. Add the `core` (and optionally `jsf`) dependency to your project.
2. Annotate beans or methods with `@InvocationMonitored`.
3. Observe `MonitoredMethodInvocationsEvent` to process collected data.

## Requirements

- Java 25+
- Maven 3.9+
- A Jakarta CDI 4.1 container (e.g. Weld 6, OpenWebBeans 4)

## Quality Plugins

The build enforces:

- **Compiler** - zero warnings (`-Xlint:all`, `failOnWarning`)
- **Enforcer** - Java/Maven version, dependency convergence, banned `javax.*` artifacts
- **Checkstyle** - import, block, coding, and whitespace rules
- **Apache RAT** - license header verification
- **JaCoCo** - code coverage reporting

## Testing

Tests use [dynamic-cdi-test-bean-addon](https://github.com/os890/dynamic-cdi-test-bean-addon)
as a Jakarta CDI test container (replacing DeltaSpike test-control). It automatically
bootstraps a CDI SE container and manages request scope for each test method.

## Build

```bash
mvn clean verify
```

## License

Licensed under the [Apache License, Version 2.0](LICENSE).
