# Use an official Node.js runtime as a parent image
FROM node:14.17.0-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Install any needed dependencies
RUN npm install

# Expose port 3000
EXPOSE 3000

# Run the command to start the server
CMD ["npm", "start"]