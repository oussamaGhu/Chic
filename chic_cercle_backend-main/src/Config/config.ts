
export default () => ({
  jwt: {
    secret: process.env.SECRET,
  },
  database: {
    connectionString: process.env.MONGO_URL,
  },
  secretGemini : process.env.SECRET_GEMINI
  
});