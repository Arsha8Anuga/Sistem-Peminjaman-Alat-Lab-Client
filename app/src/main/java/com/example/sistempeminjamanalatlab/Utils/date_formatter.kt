package com.example.sistempeminjamanalatlab.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * DateFormatter
 * Mengurus semua konversi tanggal dari/ke format database & tampilan UI.
 *
 * Format database:
 *   TIMESTAMP → "2025-01-20T14:30:00" atau "2025-01-20 14:30:00"
 *   DATE      → "2025-01-20"
 *
 * Penggunaan:
 *   DateFormatter.toDisplay("2025-01-20")           → "20 Januari 2025"
 *   DateFormatter.toDisplayWithTime("2025-01-20T14:30:00") → "20 Jan 2025, 14:30"
 *   DateFormatter.toApiFormat(Date())               → "2025-01-20"
 *   DateFormatter.isOverdue("2025-01-15")           → true
 *   DateFormatter.daysLeft("2025-01-25")            → 5
 */
object DateFormatter {

    private val locale = Locale("id", "ID")

    // ─── FORMAT INPUT dari database ──────────────────────

    private val inputFormats = listOf(
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", locale),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", locale),
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale),
        SimpleDateFormat("yyyy-MM-dd", locale)
    )

    // ─── FORMAT OUTPUT untuk tampilan UI ─────────────────

    private val displayDate         = SimpleDateFormat("dd MMMM yyyy", locale)
    private val displayDateShort    = SimpleDateFormat("dd MMM yyyy", locale)
    private val displayDateWithTime = SimpleDateFormat("dd MMM yyyy, HH:mm", locale)
    private val displayTime         = SimpleDateFormat("HH:mm", locale)
    private val apiFormat           = SimpleDateFormat("yyyy-MM-dd", locale)

    // ─── PARSER ──────────────────────────────────────────

    private fun parse(dateString: String?): Date? {
        if (dateString.isNullOrBlank()) return null
        for (format in inputFormats) {
            try {
                return format.parse(dateString)
            } catch (_: Exception) {}
        }
        return null
    }

    // ─── KONVERSI ke tampilan ─────────────────────────────

    /** "2025-01-20" → "20 Januari 2025" */
    fun toDisplay(dateString: String?): String {
        return parse(dateString)?.let { displayDate.format(it) } ?: "-"
    }

    /** "2025-01-20" → "20 Jan 2025" */
    fun toDisplayShort(dateString: String?): String {
        return parse(dateString)?.let { displayDateShort.format(it) } ?: "-"
    }

    /** "2025-01-20T14:30:00" → "20 Jan 2025, 14:30" */
    fun toDisplayWithTime(dateString: String?): String {
        return parse(dateString)?.let { displayDateWithTime.format(it) } ?: "-"
    }

    /** "2025-01-20T14:30:00" → "14:30" */
    fun toTimeOnly(dateString: String?): String {
        return parse(dateString)?.let { displayTime.format(it) } ?: "-"
    }

    // ─── KONVERSI ke format API ───────────────────────────

    /** Date() → "2025-01-20" (untuk dikirim ke API) */
    fun toApiFormat(date: Date): String = apiFormat.format(date)

    /** "20 Januari 2025" → "2025-01-20" (dari DatePicker ke API) */
    fun toApiFormatFromDisplay(displayString: String?): String? {
        if (displayString.isNullOrBlank()) return null
        return try {
            val date = displayDate.parse(displayString) ?: return null
            apiFormat.format(date)
        } catch (_: Exception) { null }
    }

    // ─── UTILITAS ─────────────────────────────────────────

    /** Cek apakah tanggal sudah lewat dari hari ini */
    fun isOverdue(dateString: String?): Boolean {
        val date = parse(dateString) ?: return false
        return date.before(Date())
    }

    /** Hitung sisa hari hingga tanggal tertentu (negatif = sudah lewat) */
    fun daysLeft(dateString: String?): Long {
        val date = parse(dateString) ?: return 0L
        val diff = date.time - Date().time
        return TimeUnit.MILLISECONDS.toDays(diff)
    }

    /** Hitung keterlambatan dalam hari (0 jika belum terlambat) */
    fun lateDays(dateString: String?): Long {
        val days = daysLeft(dateString)
        return if (days < 0) Math.abs(days) else 0L
    }

    /** Kembalikan label sisa waktu yang ramah, contoh: "Sisa 3 hari", "Terlambat 2 hari" */
    fun daysLeftLabel(dateString: String?): String {
        val days = daysLeft(dateString)
        return when {
            days > 1  -> "Sisa $days hari"
            days == 1L -> "Besok"
            days == 0L -> "Hari ini"
            else       -> "Terlambat ${Math.abs(days)} hari"
        }
    }

    /** Tanggal hari ini dalam format API */
    fun today(): String = toApiFormat(Date())

    /** Tanggal N hari dari sekarang dalam format API */
    fun daysFromNow(n: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, n)
        return toApiFormat(cal.time)
    }
}