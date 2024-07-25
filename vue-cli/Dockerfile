# Use the official node image as a parent image
FROM node:20 as builder

# Set the working directory
WORKDIR /app

# Copy package.json
COPY package.json ./

# Install dependencies
RUN yarn install


# Copy the rest of the application code
COPY . .

# Build the application
# RUN yarn run build

# Use nginx to serve the application
FROM nginx:latest

#Copy the build output to replace the default nginx contents.
#COPY --from=0 /app/dist /usr/share/nginx/html
COPY --from=builder /app/dist /app

# Expose port 3000
EXPOSE 80

# Command to run the application
#CMD ["nginx", "-g", "daemon off;"]
