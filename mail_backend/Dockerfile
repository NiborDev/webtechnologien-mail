# Build stage
FROM gradle:8-jdk24-alpine AS builder
WORKDIR /app

COPY . .

RUN gradle installDist --no-daemon


# Runtime stage
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/install/mail_backend/ /app/
RUN chmod +x /app/bin/mail_backend \
    && addgroup --system mail_backend \
    && adduser --system --ingroup mail_backend --no-create-home mail_backend \
    && chown -R mail_backend:mail_backend /app

USER mail_backend

HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 CMD curl -f http://localhost:8080/api/v1/health || exit 1

EXPOSE 8080

CMD ["/app/bin/mail_backend"]
