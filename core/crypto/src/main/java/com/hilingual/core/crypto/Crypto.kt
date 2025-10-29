package com.hilingual.core.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object Crypto {
    private const val KEY_ALIAS = "hilingual_secret_key"
    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"

    private val keyStore = KeyStore
        .getInstance("AndroidKeyStore")
        .apply {
            load(null)
        }

    private val cipher: Cipher
        get() = Cipher.getInstance(TRANSFORMATION)

    private fun createKey(): SecretKey =
        KeyGenerator
            .getInstance(ALGORITHM)
            .apply {
                init(
                    KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(BLOCK_MODE)
                        .setEncryptionPaddings(PADDING)
                        .setRandomizedEncryptionRequired(true)
                        .setUserAuthenticationRequired(false)
                        .build()
                )
            }.generateKey()

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    fun encrypt(bytes: ByteArray): ByteArray {
        val cipher = this.cipher.apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(bytes)
        return iv + encryptedBytes
    }

    fun decrypt(bytes: ByteArray): ByteArray {
        val cipher = this.cipher
        val iv = bytes.copyOfRange(0, cipher.blockSize)
        val encryptedBytes = bytes.copyOfRange(cipher.blockSize, bytes.size)
        cipher.init(
            Cipher.DECRYPT_MODE,
            getKey(),
            IvParameterSpec(iv)
        )
        return cipher.doFinal(encryptedBytes)
    }
}
