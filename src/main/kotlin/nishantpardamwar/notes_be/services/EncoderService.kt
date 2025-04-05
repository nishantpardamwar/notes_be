package nishantpardamwar.notes_be.services

import at.favre.lib.crypto.bcrypt.BCrypt

interface EncoderService {
    suspend fun hash(plainText: String): String
    suspend fun verify(plainText: String, hashedText: String): Boolean
}

class EncoderServiceImpl : EncoderService {
    override suspend fun hash(plainText: String): String {
        return BCrypt.withDefaults().hashToString(12, plainText.toCharArray())
    }

    override suspend fun verify(plainText: String, hashedText: String): Boolean {
        val result = BCrypt.verifyer().verify(plainText.toCharArray(), hashedText)
        return result.verified
    }
}