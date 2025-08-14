# delivery-frontend/Dockerfile

# Stage 1 — builder
FROM node:22 AS builder
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci --legacy-peer-deps
COPY . .
# Aquí indicamos explícitamente output en dist/browser (ajusta si tu angular.json usa otro nombre)
RUN npm run build -- --configuration=production --output-path=dist/browser

# Stage 2 — runtime nginx
FROM nginx:1.25-alpine AS runtime
RUN rm -rf /usr/share/nginx/html/*
# copiar el contenido de dist/browser (IMPORTANTE: copia los archivos dentro de browser)
COPY --from=builder /app/dist/browser/. /usr/share/nginx/html/
# tu config nginx (opcional)
COPY nginx.conf /etc/nginx/conf.d/default.conf
RUN chmod -R a+r /usr/share/nginx/html || true
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
