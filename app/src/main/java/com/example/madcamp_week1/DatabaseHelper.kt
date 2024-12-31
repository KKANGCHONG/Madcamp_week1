package com.example.madcamp_week1

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Capsule.db"

        // 테이블 이름 정의
        const val TABLE_NAME1 = "Capsule"
        const val TABLE_NAME2 = "Gallery"

        // Capsule 테이블 칼럼
        const val CAPSULE_ID = "id"
        const val CAPSULE_TITLE = "CapsuleTitle"
        const val CAPSULE_TEXT = "CapsuleText"
        const val CAPSULE_DATE = "CapsuleDate"
        const val CAPSULE_LOCATION = "CapsuleLocation"
        const val CAPSULE_IMAGES = "ImageReferences" // 이미지 참조 필드 (쉼표로 구분된 문자열)

        // Gallery 테이블 칼럼
        const val GALLERY_ID = "id"
        const val IMAGE_BYTE = "ImageByte"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Capsule 테이블 생성
        val createCapsuleTable = """CREATE TABLE $TABLE_NAME1 (
            $CAPSULE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $CAPSULE_TITLE TEXT,
            $CAPSULE_TEXT TEXT,
            $CAPSULE_DATE DATE,
            $CAPSULE_LOCATION TEXT,
            $CAPSULE_IMAGES TEXT -- 이미지 참조 필드 추가
        )""".trimIndent()

        // Gallery 테이블 생성
        val createGalleryTable = """CREATE TABLE $TABLE_NAME2 (
            $GALLERY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $IMAGE_BYTE BLOB
        )""".trimIndent()

        db.execSQL(createCapsuleTable)
        db.execSQL(createGalleryTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 기존 테이블 삭제 및 다시 생성
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME1")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME2")
        onCreate(db)
    }

    // 이미지 삽입 메서드
    fun insertImageToGallery(image: ByteArray): Long {
        return writableDatabase.use { db ->
            val values = ContentValues().apply {
                put(IMAGE_BYTE, image)
            }
            db.insert(TABLE_NAME2, null, values)
        }
    }

    // 캡슐 삽입 메서드 (이미지 참조 추가)
    fun insertCapsule(
        title: String,
        text: String,
        date: String,
        location: String,
        imageIds: List<Long> // 이미지 ID 리스트
    ): Long {
        val db = writableDatabase
        val imageReferences = imageIds.joinToString(",") // 쉼표로 구분된 문자열로 변환
        val values = ContentValues().apply {
            put(CAPSULE_TITLE, title)
            put(CAPSULE_TEXT, text)
            put(CAPSULE_DATE, date)
            put(CAPSULE_LOCATION, location)
            put(CAPSULE_IMAGES, imageReferences)
        }

        return try {
            val result = db.insert(TABLE_NAME1, null, values)
            if (result == -1L) {
                Log.e("DB_INSERT", "캡슐 삽입 실패: $values")
            } else {
                Log.d("DB_INSERT", "캡슐 삽입 성공, ID: $result")
            }
            result
        } catch (e: Exception) {
            Log.e("DB_INSERT", "캡슐 삽입 중 예외 발생: ${e.message}")
            -1L
        }
    }

    // 캡슐 ID 입력하면 각 값 리턴하는 함수들...
    // 특정 캡슐에 연결된 이미지 ID를 반환하는 메서드
    fun getImagesForCapsule(capsuleId: Long): List<Long> {
        return readableDatabase.use { db ->
            val query = "SELECT $CAPSULE_IMAGES FROM $TABLE_NAME1 WHERE $CAPSULE_ID = ?"
            val cursor = db.rawQuery(query, arrayOf(capsuleId.toString()))
            val imageIds = mutableListOf<Long>()
            if (cursor.moveToFirst()) {
                val references = cursor.getString(cursor.getColumnIndexOrThrow(CAPSULE_IMAGES))
                imageIds.addAll(references.split(",").map { it.toLong() }) // 쉼표로 분리하여 Long 리스트로 변환
            }
            cursor.close()
            imageIds
        }
    }

    fun getImageListFromIds(imageIdList: List<Long>): List<ByteArray> {
        return readableDatabase.use { db ->
            val images = mutableListOf<ByteArray>()

            for (id in imageIdList) {
                val query = "SELECT $IMAGE_BYTE FROM $TABLE_NAME2 WHERE $GALLERY_ID = ?"
                val cursor = db.rawQuery(query, arrayOf(id.toString()))
                if (cursor.moveToFirst()) {
                    val imageBlob = cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE_BYTE))
                    images.add(imageBlob)
                }
                cursor.close()
            }

            images
        }
    }

    // 특정 캡슐에 연결된 text를 반환하는 메서드
    fun getTextForCapsule(capsuleId: Long): String? {
        readableDatabase.use { db ->
            val query = "SELECT $CAPSULE_TEXT FROM $TABLE_NAME1 WHERE $CAPSULE_ID = ?"
            val cursor = db.rawQuery(query, arrayOf(capsuleId.toString()))
            return if (cursor.moveToFirst()) {
                val text = cursor.getString(cursor.getColumnIndexOrThrow(CAPSULE_TEXT))
                cursor.close()
                text
            } else {
                cursor.close()
                null}
        }
    }

    // 특정 캡슐에 연결된 title를 반환하는 메서드
    fun getTitleForCapsule(capsuleId: Long): String? {
        readableDatabase.use { db ->
            val query = "SELECT $CAPSULE_TITLE FROM $TABLE_NAME1 WHERE $CAPSULE_ID = ?"
            val cursor = db.rawQuery(query, arrayOf(capsuleId.toString()))
            return if (cursor.moveToFirst()) {
                val text = cursor.getString(cursor.getColumnIndexOrThrow(CAPSULE_TITLE))
                cursor.close()
                text
            } else {
                cursor.close()
                null}
        }
    }

    // 특정 캡슐에 연결된 date를 반환하는 메서드
    fun getDateForCapsule(capsuleId: Long): String? {
        readableDatabase.use { db ->
            val query = "SELECT $CAPSULE_DATE FROM $TABLE_NAME1 WHERE $CAPSULE_ID = ?"
            val cursor = db.rawQuery(query, arrayOf(capsuleId.toString()))
            return if (cursor.moveToFirst()) {
                val text = cursor.getString(cursor.getColumnIndexOrThrow(CAPSULE_DATE))
                cursor.close()
                text
            } else {
                cursor.close()
                null}
        }
    }

    // 특정 캡슐에 연결된 location를 반환하는 메서드 - api 되면 추가 설정 필요
    fun getLocationForCapsule(capsuleId: Long): List<Long> {
        return readableDatabase.use { db ->
            val query = "SELECT $CAPSULE_IMAGES FROM $TABLE_NAME1 WHERE $CAPSULE_ID = ?"
            val cursor = db.rawQuery(query, arrayOf(capsuleId.toString()))
            val imageIds = mutableListOf<Long>()
            if (cursor.moveToFirst()) {
                val references = cursor.getString(cursor.getColumnIndexOrThrow(CAPSULE_IMAGES))
                imageIds.addAll(references.split(",").map { it.toLong() }) // 쉼표로 분리하여 Long 리스트로 변환
            }
            cursor.close()
            imageIds
        }
    }

    // 특정 캡슐 삭제
    fun deleteCapsule(capsuleId: Long): Boolean {
        return writableDatabase.use { db ->
            val imageIds = getImagesForCapsule(capsuleId)
            for (imageId in imageIds) {
                deleteImageFromGallery(imageId)
            }
            val result = db.delete(TABLE_NAME1, "$CAPSULE_ID=?", arrayOf(capsuleId.toString()))
            result > 0
        }
    }

    // 캡슐 데이터 로그 출력
    fun logCapsules() {
        readableDatabase.use { db ->
            val query = "SELECT * FROM $TABLE_NAME1"
            val cursor = db.rawQuery(query, null)
            Log.d("DB_CONTENTSㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㄹㅁㄴㄹㅁㄴㄹㅁㄴㅇ", "Capsule 테이블 데이터:")
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(CAPSULE_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(CAPSULE_TITLE))
                val text = cursor.getString(cursor.getColumnIndexOrThrow(CAPSULE_TEXT))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(CAPSULE_DATE))
                val location = cursor.getString(cursor.getColumnIndexOrThrow(CAPSULE_LOCATION))
                val images = cursor.getString(cursor.getColumnIndexOrThrow(CAPSULE_IMAGES))
                Log.d(
                    "DB_CONTENTS",
                    "ID: $id, Title: $title, Text: $text, Date: $date, Location: $location, Images: $images"
                )
            }
            cursor.close()
        }
    }

    // 가장 최근 캡슐 ID 반환
    fun getLatestCapsuleId(): Long? {
        val db = readableDatabase
        val query = "SELECT $CAPSULE_ID FROM $TABLE_NAME1 ORDER BY $CAPSULE_ID DESC LIMIT 1"
        val cursor = db.rawQuery(query, null)

        return if (cursor.moveToFirst()) {
            val capsuleId = cursor.getLong(cursor.getColumnIndexOrThrow(CAPSULE_ID))
            cursor.close()
            capsuleId
        } else {
            cursor.close()
            null
        }
    }

    // 여기서부터는 반환한 캡슐의 데이터 수정
    fun updateCapsuleTitle(capsuleId: Long, newTitle: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(CAPSULE_TITLE, newTitle)
        }
        val result = db.update(TABLE_NAME1, values, "$CAPSULE_ID=?", arrayOf(capsuleId.toString()))
        db.close()
        return result > 0
    }

    fun updateCapsuleText(capsuleId: Long, newTitle: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(CAPSULE_TEXT, newTitle)
        }
        val result = db.update(TABLE_NAME1, values, "$CAPSULE_ID=?", arrayOf(capsuleId.toString()))
        db.close()
        return result > 0
    }

    fun updateCapsuleDate(capsuleId: Long, newTitle: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(CAPSULE_DATE, newTitle)
        }
        val result = db.update(TABLE_NAME1, values, "$CAPSULE_ID=?", arrayOf(capsuleId.toString()))
        db.close()
        return result > 0
    }

    fun updateCapsuleLocation(capsuleId: Long, newTitle: String): Boolean { // 구현되면 추가 수정 필요
        val db = writableDatabase
        val values = ContentValues().apply {
            put(CAPSULE_LOCATION, newTitle)
        }
        val result = db.update(TABLE_NAME1, values, "$CAPSULE_ID=?", arrayOf(capsuleId.toString()))
        db.close()
        return result > 0
    }

    // 특정 이미지 삭제
    fun deleteImageFromGallery(imageId: Long): Boolean {
        return writableDatabase.use { db ->
            val result = db.delete(TABLE_NAME2, "$GALLERY_ID=?", arrayOf(imageId.toString()))
            result > 0
        }
    }

    // 갤러리 테이블에서 모든 이미지 로드
    fun getAllImagesFromGallery(): List<Pair<Long, ByteArray>> {
        return readableDatabase.use { db ->
            val cursor = db.query(
                TABLE_NAME2,
                arrayOf(GALLERY_ID, IMAGE_BYTE),
                null,
                null,
                null,
                null,
                null
            )
            val images = mutableListOf<Pair<Long, ByteArray>>()
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(GALLERY_ID))
                    val image = cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE_BYTE))?: ByteArray(0) // 기본값 설정
                    images.add(Pair(id, image))
                } while (cursor.moveToNext())
            }
            cursor.close()
            images
        }
    }
}