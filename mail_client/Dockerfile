# Build the mail client application
FROM node:22-alpine AS builder
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . ./
RUN npm run build

# Webserver using Caddy to serve the mail client application
FROM caddy:2-alpine

COPY Caddyfile /etc/caddy/Caddyfile

COPY --from=builder /app/dist/mail_client/browser/ /srv/

HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 CMD curl -f http://localhost/ || exit 1

EXPOSE 80
