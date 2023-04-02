# Using Node image
FROM node:14.17.0-alpine

# Working directory
WORKDIR /app

# Copy files from VM to container:working dir
COPY . .

# Installing dependencies
RUN npm install

# Exposeing port 3000
EXPOSE 3000

# To start the server
CMD ["npm", "start"]