package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.HadithBookmarkEntity
import com.example.data.model.Hadith
import com.example.data.model.WordTranslation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HadithRepository(private val database: AppDatabase) {

    fun getBookmarksFlow(): Flow<List<HadithBookmarkEntity>> {
        return database.hadithBookmarkDao().getAllBookmarks()
    }

    suspend fun toggleFavorite(hadithId: Int, currentFav: Boolean) {
        val existing = database.hadithBookmarkDao().getBookmark(hadithId)
        val updated = existing?.copy(isFavorite = !currentFav)
            ?: HadithBookmarkEntity(
                hadithId = hadithId,
                isFavorite = !currentFav,
                memorizationLevel = 0,
                lastReadTimestamp = System.currentTimeMillis()
            )
        database.hadithBookmarkDao().insertOrUpdateBookmark(updated)
    }

    suspend fun setMemorizationLevel(hadithId: Int, level: Int) {
        val existing = database.hadithBookmarkDao().getBookmark(hadithId)
        val updated = existing?.copy(memorizationLevel = level)
            ?: HadithBookmarkEntity(
                hadithId = hadithId,
                isFavorite = false,
                memorizationLevel = level,
                lastReadTimestamp = System.currentTimeMillis()
            )
        database.hadithBookmarkDao().insertOrUpdateBookmark(updated)
    }

    suspend fun updateLastRead(hadithId: Int) {
        val existing = database.hadithBookmarkDao().getBookmark(hadithId)
        val updated = existing?.copy(lastReadTimestamp = System.currentTimeMillis())
            ?: HadithBookmarkEntity(
                hadithId = hadithId,
                isFavorite = false,
                memorizationLevel = 0,
                lastReadTimestamp = System.currentTimeMillis()
            )
        database.hadithBookmarkDao().insertOrUpdateBookmark(updated)
    }

    fun getAllHadiths(): List<Hadith> = allHadithsList

    fun getHadithById(id: Int): Hadith? {
        return allHadithsList.find { it.id == id }
    }

    fun getOverridesFlow(): Flow<List<com.example.data.local.HadithOverrideEntity>> {
        return database.hadithOverrideDao().getAllOverrides()
    }

    suspend fun saveHadithOverride(
        hadithId: Int,
        arabicText: String,
        titleId: String,
        titleSu: String,
        titleEn: String,
        translationId: String,
        translationSu: String,
        translationEn: String,
        syarahExplanation: String,
        syarahSu: String = "",
        syarahEn: String = ""
    ) {
        val entity = com.example.data.local.HadithOverrideEntity(
            hadithId = hadithId,
            arabicText = arabicText,
            titleId = titleId,
            titleSu = titleSu,
            titleEn = titleEn,
            translationId = translationId,
            translationSu = translationSu,
            translationEn = translationEn,
            syarahExplanation = syarahExplanation,
            syarahSu = syarahSu,
            syarahEn = syarahEn,
            updatedAt = System.currentTimeMillis()
        )
        database.hadithOverrideDao().saveOverride(entity)
    }

    suspend fun resetHadithToDefault(hadithId: Int) {
        database.hadithOverrideDao().deleteOverride(hadithId)
    }

    companion object {
        // Complete collection of 42 Hadith Arbain Nawawi with Arabic, Per-Word translations, and Syarah
        val allHadithsList: List<Hadith> by lazy {
            rawHadithsList.map { h ->
                val langData = HadithTranslationsData.data[h.id]
                if (langData != null) {
                    h.copy(
                        titleSu = langData.titleSu,
                        titleEn = langData.titleEn,
                        translationSu = langData.translationSu,
                        translationEn = langData.translationEn,
                        syarahSu = langData.syarahSu,
                        syarahEn = langData.syarahEn
                    )
                } else h
            }
        }

        private val rawHadithsList: List<Hadith> = listOf(
            Hadith(
                id = 1,
                number = 1,
                titleArabic = "إنما الأعمال بالنيات",
                titleId = "Niat dan Keikhlasan dalam Beramal",
                rawi = "HR. Bukhari no. 1 & Muslim no. 1907 (dari Umar bin Al-Khaththab)",
                arabicText = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى، فَمَنْ كَانَتْ هِجْرَتُهُ إِلَى اللَّهِ وَرَسُولِهِ فَهِجْرَتُهُ إِلَى اللَّهِ وَرَسُولِهِ، وَمَنْ كَانَتْ هِجْرَتُهُ لِدُنْيَا يُصِيبُهَا أَوْ امْرَأَةٍ يَنْكِحُهَا فَهِجْرَتُهُ إِلَى مَا هَاجَرَ إِلَيْهِ.",
                translationId = "Sesungguhnya setiap amalan tergantung pada niatnya, dan setiap orang akan mendapatkan apa yang ia niatkan. Barangsiapa yang hijrahnya karena Allah dan Rasul-Nya, maka hijrahnya kepada Allah dan Rasul-Nya. Dan barangsiapa yang hijrahnya karena dunia yang ingin diraihnya atau karena wanita yang ingin dinikahinya, maka hijrahnya kepada apa yang ia tuju dalam hijrahnya.",
                words = listOf(
                    WordTranslation("إِنَّمَا", "Innamā", "Sesungguhnya hanyalah"),
                    WordTranslation("الأَعْمَالُ", "Al-A'mālu", "Segala amal perbuatan"),
                    WordTranslation("بِالنِّيَّاتِ", "Bin-niyyāt", "Dengan (tergantung) niat"),
                    WordTranslation("وَإِنَّمَا", "Wa innamā", "Dan sesungguhnya"),
                    WordTranslation("لِكُلِّ امْرِئٍ", "Likulli imri'in", "Bagi setiap manusia"),
                    WordTranslation("مَا نَوَى", "Mā nawā", "Apa yang ia niatkan"),
                    WordTranslation("فَمَنْ كَانَتْ", "Faman kānat", "Maka barangsiapa yang adalah"),
                    WordTranslation("هِجْرَتُهُ", "Hijratuhū", "Hijrahnya"),
                    WordTranslation("إِلَى اللَّهِ وَرَسُولِهِ", "Ilallāhi wa rasūlih", "Kepada Allah dan Rasul-Nya"),
                    WordTranslation("لِدُنْيَا يُصِيبُهَا", "Li dun-yā yushībuhā", "Karena dunia yang ingin diraih"),
                    WordTranslation("أَوِ امْرَأَةٍ يَنْكِحُهَا", "Awim-ra'atin yankihuhā", "Atau wanita yang ingin dinikahi")
                ),
                syarahExplanation = "Hadis ini adalah salah satu poros utama agama Islam. Imam Syafi'i mengatakan hadis ini mencakup sepertiga ilmu. Niat adalah pembeda antara ibadah dan kebiasaan, serta pembeda apakah suatu amal ikhlas lillahi ta'ala atau tercampuri riya'.",
                category = "Niat & Keikhlasan"
            ),
            Hadith(
                id = 2,
                number = 2,
                titleArabic = "مراتب الدين: الإسلام والإيمان والإحسان",
                titleId = "Tingkatan Agama: Islam, Iman, dan Ihsan (Hadis Jibril)",
                rawi = "HR. Muslim no. 8 (dari Umar bin Al-Khaththab)",
                arabicText = "بَيْنَمَا نَحْنُ جُلُوْسٌ عِنْدَ رَسُوْلِ اللهِ صلى الله عليه وسلم ذَاتَ يَوْمٍ إِذْ طَلَعَ عَلَيْنَا رَجُلٌ شَدِيْدُ بَيَاضِ الثِّيَابِ شَدِيْدُ سَوَادِ الشَّعْرِ، لاَ يُرَى عَلَيْهِ أَثَرُ السَّفَرِ، وَلاَ يَعْرِفُهُ مِنَّا أَحَدٌ... قَالَ: فَأَخْبِرْنِي عَنِ الإِسْلاَمِ... وَالإِيْمَانِ... وَالإِحْسَانِ: أَنْ تَعْبُدَ اللهَ كَأَنَّكَ تَرَاهُ فَإِنْ لَمْ تَكُنْ تَرَاهُ فَإِنَّهُ يَرَاكَ.",
                translationId = "Suatu hari kami duduk bersama Rasulullah SAW, tiba-tiba muncul seorang laki-laki berpakaian sangat putih dan berambut sangat hitam, tidak tampak tanda-tanda safar padanya... Ia bertanya tentang Islam, Iman, dan Ihsan. Beliau bersabda: 'Ihsan adalah engkau beribadah kepada Allah seakan-akan engkau melihat-Nya, dan jika engkau tidak melihat-Nya, maka sesungguhnya Dia melihatmu.'",
                words = listOf(
                    WordTranslation("الإِسْلاَم", "Al-Islām", "Penyerahan diri (Rukun Islam)"),
                    WordTranslation("الإِيْمَان", "Al-Īmān", "Keyakinan hati (Rukun Iman)"),
                    WordTranslation("الإِحْسَان", "Al-Ihsān", "Kebaikan & kesempurnaan ibadah"),
                    WordTranslation("أَنْ تَعْبُدَ اللهَ", "An ta'budallāh", "Engkau menyembah Allah"),
                    WordTranslation("كَأَنَّكَ تَرَاهُ", "Ka-annaka tarāh", "Seolah-olah engkau melihat-Nya"),
                    WordTranslation("فَإِنْ لَمْ تَكُنْ تَرَاهُ", "Fa-in lam takun tarāh", "Jika engkau tak melihat-Nya"),
                    WordTranslation("فَإِنَّهُ يَرَاكَ", "Fa-innahū yarāk", "Maka sesungguhnya Dia melihatmu")
                ),
                syarahExplanation = "Hadis agung yang dikenal sebagai Hadis Jibril. Merangkum seluruh pilar Dienul Islam: Rukun Islam (amal lahiriah), Rukun Iman (keyakinan batiniah), dan Ihsan (muraqabah / penghayatan tertinggi dalam ibadah).",
                category = "Rukun Agama & Ihsan"
            ),
            Hadith(
                id = 3,
                number = 3,
                titleArabic = "بني الإسلام على خمس",
                titleId = "Rukun Islam Fondasi Agama",
                rawi = "HR. Bukhari no. 8 & Muslim no. 16 (dari Abdullah bin Umar)",
                arabicText = "بُنِيَ الإِسْلاَمُ عَلَى خَمْسٍ: شَهَادَةِ أَنْ لاَ إِلَهَ إِلاَّ اللهُ وَأَنَّ مُحَمَّدًا رَسُوْلُ اللهِ، وَإِقَامِ الصَّلاَةِ، وَإِيْتَاءِ الزَّكَاةِ، وَحَجِّ الْبَيْتِ، وَصَوْمِ رَمَضَانَ.",
                translationId = "Islam dibangun di atas lima perkara: bersaksi bahwa tidak ada tuhan yang berhak disembah selain Allah dan bahwa Muhammad adalah utusan Allah, mendirikan shalat, menunaikan zakat, haji ke Baitullah, dan puasa di bulan Ramadhan.",
                words = listOf(
                    WordTranslation("بُنِيَ", "Buniya", "Dibangun"),
                    WordTranslation("الإِسْلاَمُ", "Al-Islām", "Agama Islam"),
                    WordTranslation("عَلَى خَمْسٍ", "'Alā khamsin", "Di atas lima sendi"),
                    WordTranslation("شَهَادَةِ", "Syahādati", "Kesaksian tauhid"),
                    WordTranslation("إِقَامِ الصَّلاَةِ", "Iqāmish-shalāh", "Mendirikan shalat"),
                    WordTranslation("إِيْتَاءِ الزَّكَاةِ", "Ītā'iz-zakāh", "Menunaikan zakat"),
                    WordTranslation("حَجِّ الْبَيْتِ", "Hajjil-bait", "Haji ke Baitullah"),
                    WordTranslation("صَوْمِ رَمَضَانَ", "Shawmi ramadhān", "Puasa di bulan Ramadhan")
                ),
                syarahExplanation = "Islam diibaratkan bangunan kokoh yang bertumpu pada lima pilar pokok. Jika satu tiang rubuh, maka keutuhan bangunan tersebut menjadi terancam.",
                category = "Rukun Islam"
            ),
            Hadith(
                id = 4,
                number = 4,
                titleArabic = "خلق الإنسان وأطواره في الرحم",
                titleId = "Fase Penciptaan Manusia dan Takdir",
                rawi = "HR. Bukhari no. 3208 & Muslim no. 2643 (dari Abdullah bin Mas'ud)",
                arabicText = "إِنَّ أَحَدَكُمْ يُجْمَعُ خَلْقُهُ فِي بَطْنِ أُمِّهِ أَرْبَعِينَ يَوْمًا نُطْفَةً، ثُمَّ يَكُونُ عَلَقَةً مِثْلَ ذَلِكَ، ثُمَّ يَكُونُ مُضْغَةً مِثْلَ ذَلِكَ، ثُمَّ يُرْسَلُ إِلَيْهِ الْمَلَكُ فَيَنْفُخُ فِيهِ الرُّوحَ، وَيُؤْمَرُ بِأَرْبَعِ كَلِمَاتٍ: بِكَتْبِ رِزْقِهِ، وَأَجَلِهِ، وَعَمَلِهِ، وَشَقِيٌّ أَوْ سَعِيدٌ.",
                translationId = "Sesungguhnya setiap kalian dikumpulkan penciptaannya dalam rahim ibunya selama 40 hari berupa nutfah, kemudian menjadi segumpal darah selama itu pula, kemudian segumpal daging selama itu pula. Kemudian diutuslah malaikat untuk meniupkan ruh dan diperintahkan mencatat empat perkara: rezekinya, ajalnya, amalnya, serta celaka atau bahagianya.",
                words = listOf(
                    WordTranslation("يُجْمَعُ خَلْقُهُ", "Yujma'u khalquh", "Dikumpulkan penciptaannya"),
                    WordTranslation("نُطْفَةً", "Nuthfatan", "Setetes mani"),
                    WordTranslation("عَلَقَةً", "'Alaqatan", "Segumpal darah"),
                    WordTranslation("مُضْغَةً", "Mudghatan", "Segumpal daging"),
                    WordTranslation("فَيَنْفُخُ فِيْهِ الرُّوْحَ", "Fayanfukhu fīhir-rūh", "Maka meniupkan ruh padanya"),
                    WordTranslation("رِزْقِهِ", "Rizqih", "Rezekinya"),
                    WordTranslation("أَجَلِهِ", "Ajalih", "Ajal batas umurnya"),
                    WordTranslation("شَقِيٌّ أَوْ سَعِيْدٌ", "Syaqiyyun aw sa'īd", "Celaka atau bahagia")
                ),
                syarahExplanation = "Menjelaskan fase biologi embrio dalam kandungan yang membuktikan mukjizat kenabian, serta keimanan kepada takdir Allah (qadha dan qadar). Manusia wajib berusaha dan selalu memohon husnul khatimah.",
                category = "Akidah & Takdir"
            ),
            Hadith(
                id = 5,
                number = 5,
                titleArabic = "النهي عن الابتداع في الدين",
                titleId = "Larangan Mengada-ada dalam Urusan Agama",
                rawi = "HR. Bukhari no. 2697 & Muslim no. 1718 (dari Ummul Mukminin Aisyah)",
                arabicText = "مَنْ أَحْدَثَ فِي أَمْرِنَا هَذَا مَا لَيْسَ مِنْهُ فَهُوَ رَدٌّ. وفي رواية لمسلم: مَنْ عَمِلَ عَمَلاً لَيْسَ عَلَيْهِ أَمْرُنَا فَهُوَ رَدٌّ.",
                translationId = "Barangsiapa mengada-adakan sesuatu hal baru dalam urusan (agama) kami ini yang bukan bagian darinya, maka amalan itu tertolak. Dalam riwayat Muslim: Barangsiapa melakukan suatu amalan yang tidak ada tuntunannya dari kami, maka amalan itu tertolak.",
                words = listOf(
                    WordTranslation("مَنْ أَحْدَثَ", "Man ahdatsa", "Barangsiapa mengada-ada"),
                    WordTranslation("فِي أَمْرِنَا هَذَا", "Fī amrinā hādzā", "Dalam urusan (agama) kami ini"),
                    WordTranslation("مَا لَيْسَ مِنْهُ", "Mā laisa minhu", "Apa yang bukan darinya"),
                    WordTranslation("فَهُوَ رَدٌّ", "Fahuwa raddun", "Maka amalan itu tertolak"),
                    WordTranslation("مَنْ عَمِلَ عَمَلاً", "Man 'amila 'amalan", "Barangsiapa melakukan amalan")
                ),
                syarahExplanation = "Kaedah syariat yang sangat mendasar: syarat diterimanya amal adalah ikhlas dan ittiba' (mengikuti sunnah Rasulullah SAW). Amalan ibadah mahdhah harus berdasarkan dalil syar'i.",
                category = "Syariat & Sunnah"
            ),
            Hadith(
                id = 6,
                number = 6,
                titleArabic = "الحلال بين والحرام بين",
                titleId = "Halal, Haram, dan Menghindari Perkara Syubhat",
                rawi = "HR. Bukhari no. 52 & Muslim no. 1599 (dari An-Nu'man bin Basyir)",
                arabicText = "إِنَّ الْحَلاَلَ بَيِّنٌ وَإِنَّ الْحَرَامَ بَيِّنٌ، وَبَيْنَهُمَا أُمُوْرٌ مُشْتَبِهَاتٌ لاَ يَعْلَمُهُنَّ كَثِيْرٌ مِنَ النَّاسِ، فَمَنِ اتَّقَى الشُّبُهَاتِ فَقَدِ اسْتَبْرَأَ لِدِيْنِهِ وَعِرْضِهِ... أَلاَ وَإِنَّ فِي الْجَسَدِ مُضْغَةً إِذَا صَلَحَتْ صَلَحَ الْجَسَدُ كُلُّهُ، وَإِذَا فَسَدَتْ فَسَدَ الْجَسَدُ كُلُّهُ، أَلاَ وَهِيَ الْقَلْبُ.",
                translationId = "Sesungguhnya yang halal itu jelas dan yang haram itu jelas. Di antara keduanya ada perkara samar (syubhat) yang tidak diketahui banyak orang. Barangsiapa menjaga diri dari perkara syubhat, maka ia telah membersihkan agama dan kehormatannya... Ketahuilah dalam tubuh ada segumpal daging; jika ia baik maka baik seluruh tubuhnya, jika ia rusak maka rusak seluruh tubuhnya. Ketahuilah ia adalah hati.",
                words = listOf(
                    WordTranslation("الْحَلاَلُ بَيِّنٌ", "Al-Halālu bayyin", "Halal itu jelas"),
                    WordTranslation("الْحَرَامُ بَيِّنٌ", "Al-Harāmu bayyin", "Haram itu jelas"),
                    WordTranslation("أُمُوْرٌ مُشْتَبِهَاتٌ", "Umūrun musytabihāt", "Perkara-perkara samar"),
                    WordTranslation("فَمَنِ اتَّقَى", "Famanit-taqā", "Barangsiapa menjaga diri"),
                    WordTranslation("اسْتَبْرَأَ لِدِيْنِهِ", "Istabra'a lidīnih", "Membersihkan agamanya"),
                    WordTranslation("الْقَلْبُ", "Al-Qalbu", "Hati (qolbu)")
                ),
                syarahExplanation = "Mengajarkan sikap wara' (kehati-hatian) dalam hidup dan menjaga hati dari penyakit keserakahan duniawi dan keraguan. Kesehatan spiritual hati menentukan seluruh perilaku lahiriah seseorang.",
                category = "Wara' & Pensucian Hati"
            ),
            Hadith(
                id = 7,
                number = 7,
                titleArabic = "الدين النصيحة",
                titleId = "Agama adalah Nasihat",
                rawi = "HR. Muslim no. 55 (dari Tamim Ad-Dari)",
                arabicText = "الدِّيْنُ النَّصِيْحَةُ. قُلْنَا: لِمَنْ؟ قَالَ: لِلَّهِ وَلِكِتَابِهِ وَلِرَسُوْلِهِ وَلأَئِمَّةِ الْمُسْلِمِيْنَ وَعَامَّتِهِمْ.",
                translationId = "Agama itu adalah nasihat. Kami bertanya: 'Untuk siapa ya Rasulullah?' Beliau menjawab: 'Untuk Allah, kitab-Nya, Rasul-Nya, para pemimpin kaum muslimin, dan segenap kaum muslimin pada umumnya.'",
                words = listOf(
                    WordTranslation("الدِّيْنُ", "Ad-Dīnu", "Agama itu"),
                    WordTranslation("النَّصِيْحَةُ", "An-Nashīhatu", "Nasihat (ketulusan murni)"),
                    WordTranslation("لِلَّهِ", "Lillāh", "Untuk Allah (mentauhidkan-Nya)"),
                    WordTranslation("لِكِتَابِهِ", "Li kitābih", "Untuk kitab-Nya (Al-Qur'an)"),
                    WordTranslation("لِرَسُوْلِهِ", "Li rasūlih", "Untuk Rasul-Nya"),
                    WordTranslation("لأَئِمَّةِ الْمُسْلِمِيْنَ", "Li-a'immatil-muslimīn", "Untuk para pemimpin muslimin"),
                    WordTranslation("عَامَّتِهِمْ", "'Āmmatihim", "Masyarakat muslim umumnya")
                ),
                syarahExplanation = "Nasihat berarti ketulusan dan menghendaki kebaikan bagi yang dinasihati. Nasihat kepada Allah artinya ikhlas beribadah, kepada Al-Qur'an dengan membaca dan mengamalkan, dan kepada sesama muslim dengan saling menolong dalam kebenaran.",
                category = "Dakwah & Ukhuwah"
            ),
            Hadith(
                id = 8,
                number = 8,
                titleArabic = "حرمة دم المسلم وماله",
                titleId = "Kemuliaan dan Perlindungan Darah Serta Harta Muslim",
                rawi = "HR. Bukhari no. 25 & Muslim no. 22 (dari Ibnu Umar)",
                arabicText = "أُمِرْتُ أَنْ أُقَاتِلَ النَّاسَ حَتَّى يَشْهَدُوا أَنْ لاَ إِلَهَ إِلاَّ اللهُ وَأَنَّ مُحَمَّدًا رَسُوْلُ اللهِ، وَيُقِيْمُوا الصَّلاَةَ، وَيُؤْتُوا الزَّكَاةَ، فَإِذَا فَعَلُوا ذَلِكَ عَصَمُوا مِنِّي دِمَاءَهُمْ وَأَمْوَالَهُمْ إِلاَّ بِحَقِّ الإِسْلاَمِ، وَحِسَابُهُمْ عَلَى اللهِ.",
                translationId = "Aku diperintahkan untuk memerangi manusia hingga mereka bersaksi bahwa tidak ada sesembahan yang berhak disembah selain Allah dan Muhammad utusan Allah, mendirikan shalat, serta menunaikan zakat. Jika mereka melakukan itu, terlindunglah dariku darah dan harta mereka kecuali dengan hak Islam, dan hisab mereka terserah kepada Allah.",
                words = listOf(
                    WordTranslation("أُمِرْتُ", "Umirtu", "Aku diperintahkan"),
                    WordTranslation("حَتَّى يَشْهَدُوا", "Hattā yasy-hadū", "Hingga mereka bersaksi"),
                    WordTranslation("عَصَمُوا مِنِّي", "'Ashamū minnī", "Mereka memelihara dariku"),
                    WordTranslation("دِمَاءَهُمْ", "Dimā'ahum", "Darah-darah mereka"),
                    WordTranslation("أَمْوَالَهُمْ", "Amwālahum", "Harta benda mereka"),
                    WordTranslation("وَحِسَابُهُمْ عَلَى اللهِ", "Wa hisābuhum 'alallāh", "Dan perhitungan hisab pada Allah")
                ),
                syarahExplanation = "Kehormatan seorang muslim sangat tinggi di sisi Allah. Darah, harta, dan kehormatan seorang muslim haram dilanggar tanpa hak syariat yang sah.",
                category = "Hak Asasi & Kesucian Jiwa"
            ),
            Hadith(
                id = 9,
                number = 9,
                titleArabic = "النهي عن كثرة السؤال والتكليف بما لا يطاق",
                titleId = "Melaksanakan Perintah Semampunya dan Menjauhi Larangan",
                rawi = "HR. Bukhari no. 7288 & Muslim no. 1337 (dari Abu Hurairah)",
                arabicText = "مَا نَهَيْتُكُمْ عَنْهُ فَاجْتَنِبُوهُ، وَمَا أَمَرْتُكُمْ بِهِ فَأْتُوا مِنْهُ مَا اسْتَطَعْتُمْ، فَإِنَّمَا أَهْلَكَ الَّذِينَ مِنْ قَبْلِكُمْ كَثْرَةُ مَسَائِلِهِمْ وَاخْتِلاَفُهُمْ عَلَى أَنْبِيَائِهِمْ.",
                translationId = "Apa yang aku larang kalian darinya maka jauhilah seluruhnya, dan apa yang aku perintahkan kepada kalian maka kerjakanlah semampu kalian. Sesungguhnya yang membinasakan umat-umat sebelum kalian adalah banyaknya pertanyaan mereka dan perselisihan mereka terhadap nabi-nabi mereka.",
                words = listOf(
                    WordTranslation("مَا نَهَيْتُكُمْ", "Mā nahaitukum", "Apa yang aku larang"),
                    WordTranslation("فَاجْتَنِبُوهُ", "Fajtanibūhu", "Maka tinggalkanlah"),
                    WordTranslation("مَا أَمَرْتُكُمْ", "Mā amartukum", "Apa yang aku perintahkan"),
                    WordTranslation("مَا اسْتَطَعْتُمْ", "Māstatha'tum", "Semampu kemampuan kalian"),
                    WordTranslation("كَثْرَةُ مَسَائِلِهِمْ", "Katsratu masā'ilihim", "Banyaknya pertanyaan tak perlu")
                ),
                syarahExplanation = "Larangan harus ditinggalkan secara mutlak tanpa tawar-menawar, sementara perintah dikerjakan sesuai batas kemampuan manusiawi. Ini menunjukkan kemudahan dan rahmat dalam ajaran syariat Islam.",
                category = "Kemudahan Syariat"
            ),
            Hadith(
                id = 10,
                number = 10,
                titleArabic = "الطيب من الرزق والعمل",
                titleId = "Memakan yang Halal dan Syarat Terkabulnya Doa",
                rawi = "HR. Muslim no. 1015 (dari Abu Hurairah)",
                arabicText = "إِنَّ اللَّهَ طَيِّبٌ لاَ يَقْبَلُ إِلاَّ طَيِّبًا، وَإِنَّ اللَّهَ أَمَرَ الْمُؤْمِنِينَ بِمَا أَمَرَ بِهِ الْمُرْسَلِينَ... ثُمَّ ذَكَرَ الرَّجُلَ يُطِيلُ السَّفَرَ أَشْعَثَ أَغْبَرَ يَمُدُّ يَدَيْهِ إِلَى السَّمَاءِ: يَا رَبِّ يَا رَبِّ، وَمَطْعَمُهُ حَرَامٌ وَمَشْرَبُهُ حَرَامٌ وَمَلْبَسُهُ حَرَامٌ وَغُذِيَ بِالْحَرَامِ، فَأَنَّى يُسْتَجَابُ لِذَلِكَ؟",
                translationId = "Sesungguhnya Allah itu Maha Baik dan tidak menerima kecuali yang baik. Dan Allah memerintahkan orang-orang beriman dengan apa yang diperintahkan kepada para Rasul... Kemudian Nabi menyebutkan tentang seorang lelaki yang menempuh perjalanan jauh, berdebu dan kusut, mengangkat kedua tangannya ke langit berdoa 'Wahai Rabbku, wahai Rabbku', padahal makanannya haram, minumannya haram, pakaiannya haram, dan diberi makan dari barang haram, maka bagaimana mungkin doanya akan dikabulkan?",
                words = listOf(
                    WordTranslation("اللَّهَ طَيِّبٌ", "Allāha thayyibun", "Allah itu Maha Suci Baik"),
                    WordTranslation("لاَ يَقْبَلُ إِلاَّ طَيِّبًا", "Lā yaqbalu illā thayyibā", "Tidak menerima kecuali yang baik"),
                    WordTranslation("مَطْعَمُهُ حَرَامٌ", "Math'amuhū harām", "Makanannya haram"),
                    WordTranslation("مَشْرَبُهُ حَرَامٌ", "Masy-rabuhū harām", "Minumannya haram"),
                    WordTranslation("فَأَنَّى يُسْتَجَابُ", "Fa-annā yustajāb", "Maka bagaimana dikabulkan")
                ),
                syarahExplanation = "Makanan dan rezeki yang halal merupakan prasyarat mutlak terkabulnya doa serta diterimanya amal shalih. Barang haram menjadi hijab perintang antara hamba dan rahmat Tuhannya.",
                category = "Rezeki Halal & Doa"
            ),
            Hadith(
                id = 11,
                number = 11,
                titleArabic = "دع ما يريبك إلى ما لا يريبك",
                titleId = "Tinggalkan yang Meragukan Menuju yang Meyakinkan",
                rawi = "HR. Tirmidzi no. 2518 & An-Nasa'i no. 5711 (dari Al-Hasan bin Ali)",
                arabicText = "دَعْ مَا يَرِيْبُكَ إِلَى مَا لاَ يَرِيْبُكَ، فَإِنَّ الصِّدْقَ طُمَأْنِيْنَةٌ، وَإِنَّ الْكَذِبَ رِيْبَةٌ.",
                translationId = "Tinggalkanlah apa yang meragukanmu kepada apa yang tidak meragukanmu. Karena sesungguhnya kejujuran itu mendatangkan ketenangan, sedangkan dusta itu mendatangkan keragu-raguan.",
                words = listOf(
                    WordTranslation("دَعْ", "Da'", "Tinggalkanlah"),
                    WordTranslation("مَا يَرِيْبُكَ", "Mā yarībuk", "Apa yang meragukanmu"),
                    WordTranslation("إِلَى مَا لاَ يَرِيْبُكَ", "Ilā mā lā yarībuk", "Kepada yang tidak meragukanmu"),
                    WordTranslation("الصِّدْقَ طُمَأْنِيْنَةٌ", "Ash-shidqa thuma'nīnah", "Kejujuran itu ketenteraman"),
                    WordTranslation("الْكَذِبَ رِيْبَةٌ", "Al-kadziba rībah", "Dusta itu keraguan gelisah")
                ),
                syarahExplanation = "Kaidah fiqih dan akhlak yang membimbing hati menuju ketenangan jiwa. Bila menjumpai keraguan dalam suatu transaksi atau ucapan, pilihlah alternatif yang jelas kehalalannya.",
                category = "Wara' & Kejujuran"
            ),
            Hadith(
                id = 12,
                number = 12,
                titleArabic = "من حسن إسلام المرء تركه ما لا يعنيه",
                titleId = "Tanda Baiknya Keislaman: Meninggalkan Hal Tak Bermanfaat",
                rawi = "HR. Tirmidzi no. 2317 & Ibnu Majah no. 3976 (dari Abu Hurairah)",
                arabicText = "مِنْ حُسْنِ إِسْلاَمِ الْمَرْءِ تَرْكُهُ مَا لاَ يَعْنِيْهِ.",
                translationId = "Di antara tanda kebaikan Islam seseorang adalah ia meninggalkan apa yang tidak bermanfaat baginya (baik dalam urusan dunia maupun akhirat).",
                words = listOf(
                    WordTranslation("مِنْ حُسْنِ", "Min husni", "Sebagian dari kebaikan"),
                    WordTranslation("إِسْلاَمِ الْمَرْءِ", "Islāmil-mar'", "Keislaman seseorang"),
                    WordTranslation("تَرْكُهُ", "Tarkuhū", "Meninggalkannya dia"),
                    WordTranslation("مَا لاَ يَعْنِيْهِ", "Mā lā ya'nīh", "Apa yang tidak berguna baginya")
                ),
                syarahExplanation = "Menjaga waktu, ucapan, dan perhatian dari hal-hal sia-sia seperti ghibah, mencampuri urusan orang lain yang bukan haknya, atau bermedsos tanpa faedah.",
                category = "Adab & Manajemen Waktu"
            ),
            Hadith(
                id = 13,
                number = 13,
                titleArabic = "لا يؤمن أحدكم حتى يحب لأخيه ما يحب لنفسه",
                titleId = "Mencintai untuk Saudaranya Apa yang Dicintai untuk Dirinya",
                rawi = "HR. Bukhari no. 13 & Muslim no. 45 (dari Anas bin Malik)",
                arabicText = "لاَ يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لأَخِيْهِ مَا يُحِبُّ لِنَفْسِهِ.",
                translationId = "Tidak sempurna iman salah seorang di antara kalian hingga ia mencintai untuk saudaranya apa yang ia cintai untuk dirinya sendiri.",
                words = listOf(
                    WordTranslation("لاَ يُؤْمِنُ", "Lā yu'minu", "Tidaklah sempurna iman"),
                    WordTranslation("أَحَدُكُمْ", "Ahadukum", "Salah seorang dari kalian"),
                    WordTranslation("حَتَّى يُحِبَّ", "Hattā yuhibba", "Hingga ia mencintai"),
                    WordTranslation("لأَخِيْهِ", "Li-akhīh", "Untuk saudaranya"),
                    WordTranslation("مَا يُحِبُّ", "Mā yuhibbu", "Apa yang ia sukai"),
                    WordTranslation("لِنَفْسِهِ", "Li nafsih", "Bagi dirinya sendiri")
                ),
                syarahExplanation = "Pondasi ukhuwah islamiyah dan solidaritas sosial. Menghilangkan hasad, dengki, dan egoisme. Seorang mukmin sejati gembira tatkala saudaranya memperoleh kebaikan dan petunjuk.",
                category = "Ukhuwah & Cinta Sesama"
            ),
            Hadith(
                id = 14,
                number = 14,
                titleArabic = "حرمة دم المسلم وأسباب إهداره",
                titleId = "Larangan Menumpahkan Darah Muslim",
                rawi = "HR. Bukhari no. 6878 & Muslim no. 1676 (dari Ibnu Mas'ud)",
                arabicText = "لاَ يَحِلُّ دَمُ امْرِئٍ مُسْلِمٍ يَشْهَدُ أَنْ لاَ إِلَهَ إِلاَّ اللهُ وَأَنِّي رَسُوْلُ اللهِ إِلاَّ بِإِحْدَى ثَلاَثٍ: الثَّيِّبُ الزَّانِي، وَالنَّفْسُ بِالنَّفْسِ، وَالتَّارِكُ لِدِيْنِهِ الْمُفَارِقُ لِلْجَمَاعَةِ.",
                translationId = "Tidak halal darah seorang muslim yang bersaksi bahwa tiada tuhan selain Allah dan aku utusan-Nya kecuali karena salah satu dari tiga perkara: orang yang berzina padahal sudah menikah (muhshan), jiwa dibalas jiwa (qishash), dan orang yang meninggalkan agamanya serta memisahkan diri dari jamaah (murtad).",
                words = listOf(
                    WordTranslation("لاَ يَحِلُّ", "Lā yahillu", "Tidak halal"),
                    WordTranslation("دَمُ امْرِئٍ", "Damu imri'in", "Darah seseorang"),
                    WordTranslation("مُسْلِمٍ", "Muslimin", "Yang muslim"),
                    WordTranslation("الثَّيِّبُ الزَّانِي", "Ats-tsayyibuz-zānī", "Penzina muhshan"),
                    WordTranslation("النَّفْسُ بِالنَّفْسِ", "An-nafsu bin-nafs", "Nyawa dibalas nyawa")
                ),
                syarahExplanation = "Menegaskan betapa sucinya nyawa manusia dalam Islam. Hukum batas maksimal pidana hanya boleh ditegakkan melalui mahkamah pengadilan syariah yang resmi dan adil.",
                category = "Hukum & Keadilan"
            ),
            Hadith(
                id = 15,
                number = 15,
                titleArabic = "من كان يؤمن بالله واليوم الآخر",
                titleId = "Menjaga Lisan dan Memuliakan Tetangga Serta Tamu",
                rawi = "HR. Bukhari no. 6018 & Muslim no. 47 (dari Abu Hurairah)",
                arabicText = "مَنْ كَانَ يُؤْمِنُ بِاللَّهِ وَالْيَوْمِ الآخِرِ فَلْيَقُلْ خَيْرًا أَوْ لِيَصْمُتْ، وَمَنْ كَانَ يُؤْمِنُ بِاللَّهِ وَالْيَوْمِ الآخِرِ فَلْيُكْرِمْ جَارَهُ، وَمَنْ كَانَ يُؤْمِنُ بِاللَّهِ وَالْيَوْمِ الآخِرِ فَلْيُكْرِمْ ضَيْفَهُ.",
                translationId = "Barangsiapa beriman kepada Allah dan hari akhir, hendaklah ia berkata yang baik atau diam. Barangsiapa beriman kepada Allah dan hari akhir, hendaklah ia memuliakan tetangganya. Dan barangsiapa beriman kepada Allah dan hari akhir, hendaklah ia memuliakan tamunya.",
                words = listOf(
                    WordTranslation("فَلْيَقُلْ خَيْرًا", "Fal-yaqul khairā", "Hendaklah berkata baik"),
                    WordTranslation("أَوْ لِيَصْمُتْ", "Aw li-yashmut", "Atau hendaklah diam"),
                    WordTranslation("فَلْيُكْرِمْ جَارَهُ", "Falyukrim jārah", "Maka muliakan tetangganya"),
                    WordTranslation("فَلْيُكْرِمْ ضَيْفَهُ", "Falyukrim dhaifah", "Maka muliakan tamunya")
                ),
                syarahExplanation = "Standar etika sosial seorang mukmin. Keselamatan manusia bergantung pada bagaimana ia mengendalikan lisannya, serta kehangatan silaturahim dengan tetangga dan tamu.",
                category = "Adab Sosial & Lisan"
            ),
            Hadith(
                id = 16,
                number = 16,
                titleArabic = "لا تغضب",
                titleId = "Wasiat Jangan Marah",
                rawi = "HR. Bukhari no. 6116 (dari Abu Hurairah)",
                arabicText = "أَنَّ رَجُلاً قَالَ لِلنَّبِيِّ صلى الله عليه وسلم: أَوْصِنِي، قَالَ: لاَ تَغْضَبْ. فَرَدَّدَ مِرَارًا، قَالَ: لاَ تَغْضَبْ.",
                translationId = "Bahwa seorang laki-laki berkata kepada Nabi SAW: 'Wasiatilah aku.' Beliau menjawab: 'Janganlah engkau marah.' Lelaki itu mengulang permintaannya berulang kali, beliau tetap bersabda: 'Janganlah engkau marah.'",
                words = listOf(
                    WordTranslation("أَوْصِنِي", "Awshinī", "Berilah wasiat kepadaku"),
                    WordTranslation("لاَ تَغْضَبْ", "Lā taghdhab", "Janganlah kamu marah"),
                    WordTranslation("فَرَدَّدَ مِرَارًا", "Faraddada mirārā", "Maka ia mengulangi berkali-kali")
                ),
                syarahExplanation = "Marah adalah pintu masuk setan yang merusak akal sehat, hubungan keluarga, dan memicu penyesalan panjang. Menahan amarah adalah sifat para ksatria yang dicintai Allah.",
                category = "Manajemen Emosi & Akhlak"
            ),
            Hadith(
                id = 17,
                number = 17,
                titleArabic = "إن الله كتب الإحسان على كل شيء",
                titleId = "Berbuat Ihsan dalam Segala Hal Termasuk Menyembelih",
                rawi = "HR. Muslim no. 1955 (dari Syaddad bin Aus)",
                arabicText = "إِنَّ اللَّهَ كَتَبَ الإِحْسَانَ عَلَى كُلِّ شَيْءٍ، فَإِذَا قَتَلْتُمْ فَأَحْسِنُوا الْقِتْلَةَ، وَإِذَا ذَبَحْتُمْ فَأَحْسِنُوا الذِّبْحَةَ، وَلْيُحِدَّ أَحَدُكُمْ شَفْرَتَهُ، وَلْيُرِحْ ذَبِيحَتَهُ.",
                translationId = "Sesungguhnya Allah mewajibkan berlaku ihsan (berbuat baik) dalam segala hal. Maka jika kalian membunuh (dalam qishash/perang yang sah), perbaguslah cara membunuhnya, dan jika kalian menyembelih hewan, perbaguslah cara menyembelihnya; hendaklah salah seorang dari kalian menajamkan pisaunya dan memberi kenyamanan pada hewan sembelihannya.",
                words = listOf(
                    WordTranslation("كَتَبَ الإِحْسَانَ", "Katabal-ihsān", "Mewajibkan berbuat ihsan"),
                    WordTranslation("عَلَى كُلِّ شَيْءٍ", "'Alā kulli syai'", "Atas segala sesuatu"),
                    WordTranslation("وَلْيُحِدَّ شَفْرَتَهُ", "Walyuhidda syafratah", "Dan tajamkanlah pisaunya"),
                    WordTranslation("وَلْيُرِحْ ذَبِيحَتَهُ", "Walyurih dzabīhatah", "Dan tenangkanlah sembelihannya")
                ),
                syarahExplanation = "Agama Islam adalah rahmatan lil 'alamin bahkan terhadap hewan. Profesionalitas dan kelembutan wajib ditegakkan dalam setiap proses pekerjaan dan ibadah.",
                category = "Kasih Sayang & Ihsan"
            ),
            Hadith(
                id = 18,
                number = 18,
                titleArabic = "اتق الله حيثما كنت",
                titleId = "Bertaqwa di Mana Saja dan Berakhlak Mulia",
                rawi = "HR. Tirmidzi no. 1987 (dari Abu Dzar & Mu'adz bin Jabal)",
                arabicText = "اتَّقِ اللَّهَ حَيْثُمَا كُنْتَ، وَأَتْبِعِ السَّيِّئَةَ الْحَسَنَةَ تَمْحُهَا، وَخَالِقِ النَّاسَ بِخُلُقٍ حَسَنٍ.",
                translationId = "Bertaqwalah kepada Allah di mana pun engkau berada, ikutilah perbuatan buruk dengan perbuatan baik niscaya kebaikan itu akan menghapusnya, dan pergauililah manusia dengan akhlak yang mulia.",
                words = listOf(
                    WordTranslation("اتَّقِ اللَّهَ", "Ittaqillāh", "Bertaqwalah kepada Allah"),
                    WordTranslation("حَيْثُمَا كُنْتَ", "Haitsumā kunta", "Di mana pun engkau berada"),
                    WordTranslation("وَأَتْبِعِ السَّيِّئَةَ", "Wa atbi'is-sayyi'ah", "Dan ikutilah keburukan"),
                    WordTranslation("الْحَسَنَةَ تَمْحُهَا", "Al-hasanata tamhuhā", "Dengan kebaikan yang menghapusnya"),
                    WordTranslation("بِخُلُقٍ حَسَنٍ", "Bi khuluqin hasan", "Dengan perangai akhlak terpuji")
                ),
                syarahExplanation = "Tiga wasiat emas: hubungan vertikal dengan Allah (taqwa kapan dan di mana saja), introspeksi bertaubat memperbaiki kesalahan, serta hubungan horizontal berakhlak mulia sesama manusia.",
                category = "Taqwa & Akhlak Terpuji"
            ),
            Hadith(
                id = 19,
                number = 19,
                titleArabic = "احفظ الله يحفظك",
                titleId = "Jagalah Allah Niscaya Allah Menjagamu",
                rawi = "HR. Tirmidzi no. 2516 (dari Abdullah bin Abbas)",
                arabicText = "يَا غُلاَمُ إِنِّي أُعَلِّمُكَ كَلِمَاتٍ: احْفَظِ اللَّهَ يَحْفَظْكَ، احْفَظِ اللَّهَ تَجِدْهُ تُجَاهَكَ، إِذَا سَأَلْتَ فَاسْأَلِ اللَّهَ، وَإِذَا اسْتَعَنْتَ فَاسْتَعِنْ بِاللَّهِ، وَاعْلَمْ أَنَّ الأُمَّةَ لَوِ اجْتَمَعَتْ عَلَى أَنْ يَنْفَعُوكَ بِشَيْءٍ لَمْ يَنْفَعُوكَ إِلاَّ بِشَيْءٍ قَدْ كَتَبَهُ اللَّهُ لَكَ...",
                translationId = "Wahai anak muda, sesungguhnya aku mengajarkan kepadamu beberapa kalimat: Jagalah Allah niscaya Dia akan menjagamu. Jagalah Allah niscaya engkau mendapati-Nya di hadapanmu. Jika engkau meminta, mintalah kepada Allah. Jika engkau memohon pertolongan, mohonlah pertolongan kepada Allah. Ketahuilah sekiranya seluruh umat berkumpul untuk memberimu manfaat, mereka tidak bisa kecuali apa yang telah Allah tetapkan untukmu...",
                words = listOf(
                    WordTranslation("احْفَظِ اللَّهَ", "Ihfazhillāh", "Jagalah batasan perintah Allah"),
                    WordTranslation("يَحْفَظْكَ", "Yahfazhka", "Niscaya Dia melindungimu"),
                    WordTranslation("فَاسْأَلِ اللَّهَ", "Fas'alillāh", "Maka mintalah kepada Allah"),
                    WordTranslation("فَاسْتَعِنْ بِاللَّهِ", "Fasta'in billāh", "Maka mintalah tolong pada Allah"),
                    WordTranslation("رُفِعَتِ الأَقْلاَمُ", "Rufi'atil-aqlām", "Pena takdir telah diangkat")
                ),
                syarahExplanation = "Pedoman tauhid yang memerdekakan jiwa dari ketergantungan kepada selain Allah. Segala takdir, manfaat, dan mudharat berada sepenuhnya di tangan kekuasaan Allah SWT.",
                category = "Tauhid & Tawakkal"
            ),
            Hadith(
                id = 20,
                number = 20,
                titleArabic = "إذا لم تستح فاصنع ما شئت",
                titleId = "Rasa Malu Bagian dari Iman",
                rawi = "HR. Bukhari no. 3484 (dari Abu Mas'ud Uqbah bin Amr)",
                arabicText = "إِنَّ مِمَّا أَدْرَكَ النَّاسُ مِنْ كَلاَمِ النُّبُوَّةِ الأُولَى: إِذَا لَمْ تَسْتَحِ فَاصْنَعْ مَا شِئْتَ.",
                translationId = "Sesungguhnya salah satu ungkapan yang didapati manusia dari warisan kenabian terdahulu adalah: 'Jika engkau sudah tidak punya rasa malu, maka perbuatlah sesukamu.'",
                words = listOf(
                    WordTranslation("النُّبُوَّةِ الأُولَى", "An-nubuwwatil-ūlā", "Kenabian yang terdahulu"),
                    WordTranslation("إِذَا لَمْ تَسْتَحِ", "Idzā lam tastahi", "Jika engkau tidak punya malu"),
                    WordTranslation("فَاصْنَعْ مَا شِئْتَ", "Fashna' mā syi'ta", "Maka perbuatlah semaumu")
                ),
                syarahExplanation = "Rasa malu kepada Allah dan manusia adalah benteng pertahanan moral tertinggi. Ketika rasa malu runtuh, pintu segala maksiat akan terbuka lebar.",
                category = "Rasa Malu & Moralitas"
            ),
            Hadith(
                id = 21,
                number = 21,
                titleArabic = "قل آمنت بالله ثم استقم",
                titleId = "Katakanlah Aku Beriman Lalu Beristiqamah",
                rawi = "HR. Muslim no. 38 (dari Sufyan bin Abdullah)",
                arabicText = "قُلْتُ: يَا رَسُولَ اللَّهِ، قُلْ لِي فِي الإِسْلاَمِ قَوْلاً لاَ أَسْأَلُ عَنْهُ أَحَدًا غَيْرَكَ. قَالَ: قُلْ: آمَنْتُ بِاللَّهِ ثُمَّ اسْتَقِمْ.",
                translationId = "Aku berkata: 'Wahai Rasulullah, katakanlah kepadaku dalam Islam suatu perkataan yang tidak perlu kutanyakan lagi kepada orang lain selainmu.' Beliau bersabda: 'Katakanlah: Aku beriman kepada Allah, lalu beristiqamahlah.'",
                words = listOf(
                    WordTranslation("قُلْ آمَنْتُ", "Qul āmantu", "Katakanlah: aku beriman"),
                    WordTranslation("بِاللَّهِ", "Billāh", "Kepada Allah"),
                    WordTranslation("ثُمَّ اسْتَقِمْ", "Tsummas-taqim", "Kemudian istiqamahlah teguh")
                ),
                syarahExplanation = "Kunci keselamatan dunia akhirat adalah perpaduan antara tauhid yang lurus dengan konsistensi (istiqamah) dalam menjalankan ketaatan hingga akhir hayat.",
                category = "Istiqamah & Keteguhan"
            ),
            Hadith(
                id = 22,
                number = 22,
                titleArabic = "طريق الجنة بالفرائض",
                titleId = "Jalan Menuju Surga dengan Menjaga yang Wajib",
                rawi = "HR. Muslim no. 15 (dari Jabir bin Abdillah)",
                arabicText = "أَرَأَيْتَ إِذَا صَلَّيْتُ الْمَكْتُوبَاتِ، وَصُمْتُ رَمَضَانَ، وَأَحْلَلْتُ الْحَلاَلَ، وَحَرَّمْتُ الْحَرَامَ، وَلَمْ أَزِدْ عَلَى ذَلِكَ شَيْئًا، أَأَدْخُلُ الْجَنَّةَ؟ قَالَ: نَعَمْ.",
                translationId = "Seorang sahabat bertanya: 'Bagaimana pendapatmu jika aku telah melaksanakan shalat lima waktu yang wajib, berpuasa di bulan Ramadhan, menghalalkan yang halal, mengharamkan yang haram, dan aku tidak menambah amalan selain itu, apakah aku akan masuk surga?' Nabi menjawab: 'Ya.'",
                words = listOf(
                    WordTranslation("الْمَكْتُوبَاتِ", "Al-maktūbāt", "Shalat-shalat fardhu"),
                    WordTranslation("أَحْلَلْتُ الْحَلاَلَ", "Ahlaltul-halāl", "Menghalalkan yang halal"),
                    WordTranslation("حَرَّمْتُ الْحَرَامَ", "Harramtul-harām", "Menjauhi yang haram"),
                    WordTranslation("أَأَدْخُلُ الْجَنَّةَ", "A-adkhulul-jannah", "Apakah aku masuk surga?"),
                    WordTranslation("نَعَمْ", "Na'am", "Ya (benar)")
                ),
                syarahExplanation = "Menunjukkan prioritas utama dalam Islam adalah menjaga kewajiban fardhu secara paripurna dan menjauhi yang diharamkan Allah sebelum melangkah ke amalan sunnah.",
                category = "Kewajiban Pokok"
            ),
            Hadith(
                id = 23,
                number = 23,
                titleArabic = "الطهور شطر الإيمان",
                titleId = "Kesucian Sebagian dari Iman",
                rawi = "HR. Muslim no. 223 (dari Abu Malik Al-Asy'ari)",
                arabicText = "الطُّهُوْرُ شَطْرُ الإِيْمَانِ، وَالْحَمْدُ لِلَّهِ تَمْلأُ الْمِيْزَانَ، وَسُبْحَانَ اللهِ وَالْحَمْدُ لِلَّهِ تَمْلآنِ - أَوْ تَمْلأُ - مَا بَيْنَ السَّمَاءِ وَالأَرْضِ، وَالصَّلاَةُ نُوْرٌ، وَالصَّدَقَةُ بُرْهَانٌ، وَالصَّبْرُ ضِيَاءٌ، وَالْقُرْآنُ حُجَّةٌ لَكَ أَوْ عَلَيْكَ، كُلُّ النَّاسِ يَغْدُوْ فَبَائِعٌ نَفْسَهُ فَمُعْتِقُهَا أَوْ مُوْبِقُهَا.",
                translationId = "Bersuci adalah separuh dari keimanan, kalimat alhamdulillah memenuhi timbangan kebaikan, subhanallah dan alhamdulillah memenuhi antara langit dan bumi, shalat adalah cahaya, sedekah adalah bukti, sabar adalah pelita yang membakar, dan Al-Qur'an adalah saksi yang membelamu atau menuntutmu. Setiap manusia berpagi hari memperdagangkan dirinya; ada yang memerdekakan jiwanya dan ada yang membinasakannya.",
                words = listOf(
                    WordTranslation("الطُّهُوْرُ", "Ath-Thuhūru", "Bersuci lahir batin"),
                    WordTranslation("شَطْرُ الإِيْمَانِ", "Syathrul-īmān", "Separuh dari iman"),
                    WordTranslation("الصَّلاَةُ نُوْرٌ", "Ash-shalātu nūr", "Shalat adalah cahaya"),
                    WordTranslation("الصَّدَقَةُ بُرْهَانٌ", "Ash-shadaqatu burhān", "Sedekah adalah bukti nyata"),
                    WordTranslation("الصَّبْرُ ضِيَاءٌ", "Ash-shabru dhiyā'", "Sabar adalah sinar penerang")
                ),
                syarahExplanation = "Hadis komprehensif tentang thaharah, dzikir, shalat, sedekah, dan konsekuensi pilihan hidup manusia di hadapan Al-Qur'an kelak di yaumil akhir.",
                category = "Thaharah & Dzikir"
            ),
            Hadith(
                id = 24,
                number = 24,
                titleArabic = "تحريم الظلم",
                titleId = "Haramnya Berbuat Zalim (Hadis Qudsi)",
                rawi = "HR. Muslim no. 2577 (dari Abu Dzar Al-Ghifari)",
                arabicText = "يَا عِبَادِي إِنِّي حَرَّمْتُ الظُّلْمَ عَلَى نَفْسِي وَجَعَلْتُهُ بَيْنَكُمْ مُحَرَّمًا فَلاَ تَظَالَمُوا، يَا عِبَادِي كُلُّكُمْ ضَالٌّ إِلاَّ مَنْ هَدَيْتُهُ فَاسْتَهْدُونِي أَهْدِكُمْ، يَا عِبَادِي كُلُّكُمْ جَائِعٌ إِلاَّ مَنْ أَطْعَمْتُهُ فَاسْتَطْعِمُونِي أُطْعِمْكُمْ...",
                translationId = "Wahai hamba-hamba-Ku, sesungguhnya Aku mengharamkan kezaliman atas diri-Ku dan Aku menjadikannya haram di antara kalian, maka janganlah kalian saling menzalimi! Wahai hamba-Ku, kalian semua tersesat kecuali yang Kuberi petunjuk, maka mintalah petunjuk kepada-Ku niscaya Kuberi petunjuk...",
                words = listOf(
                    WordTranslation("حَرَّمْتُ الظُّلْمَ", "Harramtuzh-zhulma", "Aku haramkan kezaliman"),
                    WordTranslation("فَلاَ تَظَالَمُوا", "Falā tazhālamū", "Maka jangan saling menzalimi"),
                    WordTranslation("فَاسْتَهْدُونِي", "Fastahdūnī", "Mintalah petunjuk kepada-Ku"),
                    WordTranslation("أَهْدِكُمْ", "Ahdikum", "Niscaya Aku beri kalian hidayah")
                ),
                syarahExplanation = "Salah satu Hadis Qudsi teragung. Menjelaskan sifat kemahakayaan Allah dan betapa butuhnya seluruh makhluk kepada rahmat, rezeki, ampunan, dan hidayah-Nya.",
                category = "Keadilan & Hadis Qudsi"
            ),
            Hadith(
                id = 25,
                number = 25,
                titleArabic = "فضل الذكر والصدقة المعنوية",
                titleId = "Keluasan Pintu-Pintu Sedekah",
                rawi = "HR. Muslim no. 1006 (dari Abu Dzar Al-Ghifari)",
                arabicText = "إِنَّ بِكُلِّ تَسْبِيْحَةٍ صَدَقَةً، وَكُلِّ تَكْبِيْرَةٍ صَدَقَةً، وَكُلِّ تَحْمِيْدَةٍ صَدَقَةً، وَكُلِّ تَهْلِيْلَةٍ صَدَقَةً، وَأَمْرٌ بِالْمَعْرُوْفِ صَدَقَةٌ، وَنَهْيٌ عَنْ مُنْكَرٍ صَدَقَةٌ، وَفِي بُضْعِ أَحَدِكُمْ صَدَقَةٌ.",
                translationId = "Sesungguhnya pada setiap tasbih adalah sedekah, setiap takbir adalah sedekah, setiap tahmid adalah sedekah, setiap tahlil adalah sedekah, amar ma'ruf adalah sedekah, nahi munkar adalah sedekah, dan pada hubungan suami istri halal kalian ada sedekah.",
                words = listOf(
                    WordTranslation("بِكُلِّ تَسْبِيْحَةٍ", "Bi kulli tasbīhah", "Pada setiap tasbih"),
                    WordTranslation("صَدَقَةٌ", "Shadaqatun", "Bernilai sedekah"),
                    WordTranslation("أَمْرٌ بِالْمَعْرُوْفِ", "Amrun bil-ma'rūf", "Mengajak kebaikan"),
                    WordTranslation("نَهْيٌ عَنْ مُنْكَرٍ", "Nahyun 'an munkar", "Mencegah kemungkaran")
                ),
                syarahExplanation = "Sedekah tidak terbatas pada materi atau harta uang. Setiap perbuatan baik, dzikir lisan, dan menjaga kehormatan diri bersama keluarga dicatat sebagai shadaqah berlipat ganda.",
                category = "Sedekah & Amal Shalih"
            ),
            Hadith(
                id = 26,
                number = 26,
                titleArabic = "حقوق الأعضاء والصلح بين الناس",
                titleId = "Sedekah Setiap Persendian dan Mendamaikan Manusia",
                rawi = "HR. Bukhari no. 2989 & Muslim no. 1009 (dari Abu Hurairah)",
                arabicText = "كُلُّ سُلاَمَى مِنَ النَّاسِ عَلَيْهِ صَدَقَةٌ كُلَّ يَوْمٍ تَطْلُعُ فِيْهِ الشَّمْسُ: تَعْدِلُ بَيْنَ الاِثْنَيْنِ صَدَقَةٌ، وَتُعِيْنُ الرَّجُلَ فِي دَابَّتِهِ فَتَحْمِلُهُ عَلَيْهَا أَوْ تَرْفَعُ لَهُ عَلَيْهَا مَتَاعَهُ صَدَقَةٌ، وَالْكَلِمَةُ الطَّيِّبَةُ صَدَقَةٌ، وَبِكُلِّ خَطْوَةٍ تَمْشِيْهَا إِلَى الصَّلاَةِ صَدَقَةٌ، وَتُمِيْطُ الأَذَى عَنِ الطَّرِيْقِ صَدَقَةٌ.",
                translationId = "Setiap persendian manusia memiliki kewajiban sedekah setiap hari matahari terbit: mendamaikan dua orang yang berselisih secara adil adalah sedekah, menolong seseorang naik kendaraannya atau menaikkan barang bawaannya adalah sedekah, kata-kata yang baik adalah sedekah, setiap langkah kaki menuju shalat adalah sedekah, dan menyingkirkan duri/gangguan dari jalan adalah sedekah.",
                words = listOf(
                    WordTranslation("كُلُّ سُلاَمَى", "Kullu sulāmā", "Setiap ruas persendian"),
                    WordTranslation("تَعْدِلُ بَيْنَ الاِثْنَيْنِ", "Ta'dilu bainats-nain", "Mendamaikan dua orang adil"),
                    WordTranslation("الْكَلِمَةُ الطَّيِّبَةُ", "Al-kalimatuth-thayyibah", "Tutur kata yang santun baik"),
                    WordTranslation("تُمِيْطُ الأَذَى", "Tumīthul-adzā", "Menyingkirkan rintangan")
                ),
                syarahExplanation = "Manusia memiliki 360 persendian yang patut disyukuri setiap hari melalui kepedulian sosial, kata santun, menjaga kebersihan ruang publik, serta melangkah ke masjid.",
                category = "Kepedulian Sosial & Fasilitas Umum"
            ),
            Hadith(
                id = 27,
                number = 27,
                titleArabic = "البر حسن الخلق والإثم ما حاك في الصدر",
                titleId = "Kebaikan adalah Akhlak Mulia dan Dosa Menggelisahkan Hati",
                rawi = "HR. Muslim no. 2553 (dari An-Nawwas bin Sam'an & Wabishah)",
                arabicText = "الْبِرُّ حُسْنُ الْخُلُقِ، وَالإِثْمُ مَا حَاكَ فِي نَفْسِكَ وَكَرِهْتَ أَنْ يَطَّلِعَ عَلَيْهِ النَّاسُ. وفي رواية وابصة: اسْتَفْتِ قَلْبَكَ، الْبِرُّ مَا اطْمَأَنَّتْ إِلَيْهِ النَّفْسُ وَاطْمَأَنَّ إِلَيْهِ الْقَلْبُ، وَالإِثْمُ مَا حَاكَ فِي النَّفْسِ وَتَرَدَّدَ فِي الصَّدْرِ، وَإِنْ أَفْتَاكَ النَّاسُ وَأَفْتَوْكَ.",
                translationId = "Kebaikan adalah keluhuran budi pekerti (akhlak terpuji), sedangkan dosa adalah apa yang mengganjal dan menggelisahkan jiwamu serta engkau tidak suka jika manusia lain melihatnya. Dalam riwayat Wabishah: Mintalah fatwa kepada nurani hatimu; kebaikan adalah yang membuat jiwa dan hatimu tentram, sedangkan dosa adalah yang meragukan dan bergejolak di dada sekalipun orang-orang berulang kali memberi fatwa kepadamu.",
                words = listOf(
                    WordTranslation("الْبِرُّ", "Al-Birru", "Kebaikan sejati"),
                    WordTranslation("حُسْنُ الْخُلُقِ", "Husnul-khuluq", "Akhlak budi pekerti mulia"),
                    WordTranslation("الإِثْمُ", "Al-Itsmu", "Dosa keburukan"),
                    WordTranslation("اسْتَفْتِ قَلْبَكَ", "Istafti qalbak", "Tanyalah nurani hatimu"),
                    WordTranslation("تَرَدَّدَ فِي الصَّدْرِ", "Taraddada fish-shadr", "Berguncang ragu di dada")
                ),
                syarahExplanation = "Hati nurani seorang mukmin yang suci memiliki radar batin yang sensitif terhadap kemaksiatan. Kebaikan memancarkan ketenangan, sedangkan dosa memicu kegelisahan.",
                category = "Nurani & Akhlak"
            ),
            Hadith(
                id = 28,
                number = 28,
                titleArabic = "التمسك بالسنة ولزوم الجماعة",
                titleId = "Berpegang Teguh pada Sunnah dan Menjauhi Hal Baru",
                rawi = "HR. Abu Dawud no. 4607 & Tirmidzi no. 2676 (dari Al-Irbadh bin Sariyah)",
                arabicText = "وَعَظَنَا رَسُوْلُ اللهِ مَوْعِظَةً وَجِلَتْ مِنْهَا الْقُلُوْبُ وَذَرَفَتْ مِنْهَا الْعُيُوْنُ... فَعَلَيْكُمْ بِسُنَّتِي وَسُنَّةِ الْخُلَفَاءِ الرَّاشِدِيْنَ الْمَهْدِيِّيْنَ عَضُّوا عَلَيْهَا بِالنَّوَاجِذِ، وَإِيَّاكُمْ وَمُحْدَثَاتِ الأُمُوْرِ فَإِنَّ كُلَّ بِدْعَةٍ ضَلاَلَةٌ.",
                translationId = "Rasulullah memberi kami nasihat mendalam yang membuat hati bergetar dan air mata berlinang... 'Maka hendaklah kalian berpegang teguh pada sunnahku dan sunnah para Khulafaur Rasyidin yang mendapat petunjuk, gigitlah sunnah itu erat-erat dengan gigi geraham kalian, dan berhati-hatilah dari perkara baru yang diada-adakan karena setiap bid'ah adalah sesat.'",
                words = listOf(
                    WordTranslation("مَوْعِظَةً بَلِيْغَةً", "Maw'izhatan balīghah", "Nasihat yang sangat mendalam"),
                    WordTranslation("عَضُّوا عَلَيْهَا", "'Adhdhū 'alaihā", "Gigitlah berpegang kuat"),
                    WordTranslation("بِالنَّوَاجِذِ", "Bin-nawājidz", "Dengan gigi geraham"),
                    WordTranslation("كُلَّ بِدْعَةٍ ضَلاَلَةٌ", "Kulla bid'atin dhalālah", "Setiap bid'ah kesesatan")
                ),
                syarahExplanation = "Peringatan agung bagi keselamatan generasi penerus di saat terjadi banyak perpecahan dan fitnah zaman. Konsisten dengan petunjuk Nabi SAW dan para sahabat adalah bahtera keselamatan.",
                category = "Sunnah & Khulafaur Rasyidin"
            ),
            Hadith(
                id = 29,
                number = 29,
                titleArabic = "أبواب الخير والنجاة من النار",
                titleId = "Pintu-Pintu Kebaikan dan Mengendalikan Lidah",
                rawi = "HR. Tirmidzi no. 2616 (dari Mu'adz bin Jabal)",
                arabicText = "قُلْتُ: يَا رَسُولَ اللَّهِ، أَخْبِرْنِي بِعَمَلٍ يُدْخِلُنِي الْجَنَّةَ وَيُبَاعِدُنِي عَنِ النَّارِ... ثُمَّ قَالَ: أَلاَ أُخْبِرُكَ بِمَلاَكِ ذَلِكَ كُلِّهِ؟ قُلْتُ: بَلَى يَا نَبِيَّ اللَّهِ، فَأَخَذَ بِلِسَانِهِ قَالَ: كُفَّ عَلَيْكَ هَذَا. قُلْتُ: وَإِنَّا لَمُؤَاخَذُونَ بِمَا نَتَكَلَّمُ بِهِ؟ فَقَالَ: ثَكِلَتْكَ أُمُّكَ يَا مُعَاذُ، وَهَلْ يَكُبُّ النَّاسَ فِي النَّارِ عَلَى وُجُوهِهِمْ إِلاَّ حَصَائِدُ أَلْسِنَتِهِمْ؟",
                translationId = "Aku berkata: 'Wahai Rasulullah beritahu aku amal yang memasukkanku ke surga dan menjauhkanku dari neraka'... Beliau bersabda: 'Maukah kuberitahu kunci pengendali semua itu?' Beliau memegang lidahnya seraya bersabda: 'Tahanlah lisanmu ini!' Aku bertanya: 'Apakah kita disiksa karena apa yang kita ucapkan?' Beliau bersabda: 'Bukankah yang menjerumuskan manusia ke dalam neraka tertelungkup di atas wajahnya melainkan hasil buah petikan lidah mereka?'",
                words = listOf(
                    WordTranslation("بِمَلاَكِ ذَلِكَ كُلِّهِ", "Bi malāki dzālika kullih", "Kunci kendali dari semua itu"),
                    WordTranslation("كُفَّ عَلَيْكَ هَذَا", "Kuffa 'alaika hādzā", "Kendalikanlah lisan ini"),
                    WordTranslation("حَصَائِدُ أَلْسِنَتِهِمْ", "Hashā'idu alsinatihim", "Hasil panen racun lisan mereka")
                ),
                syarahExplanation = "Lidah tampak kecil namun akibat dosanya (ghibah, fitnah, namimah, caci maki) sanggup menghapus timbangan amal shalih dan menyeret pemiliknya ke dalam azab neraka.",
                category = "Bahaya Lisan & Kunci Surga"
            ),
            Hadith(
                id = 30,
                number = 30,
                titleArabic = "حدود الله وفرائضه",
                titleId = "Batasan-Batasan Hukum Allah",
                rawi = "HR. Ad-Daraquthni no. 4396 (dari Abu Tsa'labah Al-Khusyani)",
                arabicText = "إِنَّ اللَّهَ تَعَالَى فَرَضَ فَرَائِضَ فَلاَ تُضَيِّعُوهَا، وَحَدَّ حُدُودًا فَلاَ تَعْتَدُوهَا، وَحَرَّمَ أَشْيَاءَ فَلاَ تَنْتَهِكُوهَا، وَسَكَتَ عَنْ أَشْيَاءَ رَحْمَةً لَكُمْ غَيْرَ نِسْيَانٍ فَلاَ تَبْحَثُوا عَنْهَا.",
                translationId = "Sesungguhnya Allah Ta'ala telah mewajibkan kewajiban-kewajiban maka janganlah kalian sia-siakan, dan menetapkan batas-batas hukum maka janganlah kalian lampaui, dan mengharamkan hal-hal maka jangan kalian langgar, dan Dia mendiamkan sebagian perkara sebagai rahmat bagi kalian tanpa lupa, maka janganlah kalian memaksakan mencari-cari hukumnya.",
                words = listOf(
                    WordTranslation("فَرَضَ فَرَائِضَ", "Faradha farā'idha", "Mewajibkan hal yang fardhu"),
                    WordTranslation("وَحَدَّ حُدُودًا", "Wa hadda hudūdā", "Menetapkan batas hukum"),
                    WordTranslation("فَلاَ تَعْتَدُوهَا", "Falā ta'tadūhā", "Janganlah melampauinya"),
                    WordTranslation("رَحْمَةً لَكُمْ", "Rahmatan lakum", "Sebagai kasih sayang rahmat-Nya")
                ),
                syarahExplanation = "Klasifikasi hukum Islam yang jelas dan proporsional. Sesuatu yang didiamkan syariat merupakan keringanan dan rahmat (ashal hukum asal perkara duniawi adalah mubah).",
                category = "Batasan Syariat & Rahmat"
            ),
            Hadith(
                id = 31,
                number = 31,
                titleArabic = "حقيقة الزهد في الدنيا",
                titleId = "Hakikat Zuhud Mendapat Cinta Allah dan Manusia",
                rawi = "HR. Ibnu Majah no. 4102 (dari Sahl bin Sa'ad As-Sa'idi)",
                arabicText = "أَتَى النَّبِيَّ صلى الله عليه وسلم رَجُلٌ فَقَالَ: يَا رَسُولَ اللَّهِ، دُلَّنِي عَلَى عَمَلٍ إِذَا عَمِلْتُهُ أَحَبَّنِي اللَّهُ وَأَحَبَّنِي النَّاسُ. فَقَالَ: ازْهَدْ فِي الدُّنْيَا يُحِبَّكَ اللَّهُ، وَازْهَدْ فِيمَا فِي أَيْدِي النَّاسِ يُحِبَّكَ النَّاسُ.",
                translationId = "Seorang lelaki datang kepada Nabi SAW dan berkata: 'Wahai Rasulullah, tunjukkanlah kepadaku suatu amalan yang apabila kukerjakan maka Allah mencintaiku dan manusia pun mencintaiku.' Beliau bersabda: 'Zuhudlah terhadap dunia niscaya Allah mencintaimu, dan zuhudlah terhadap apa yang ada di tangan manusia niscaya manusia akan mencintaimu.'",
                words = listOf(
                    WordTranslation("ازْهَدْ فِي الدُّنْيَا", "Izhad fid-dunyā", "Zuhudlah terhadap dunia"),
                    WordTranslation("يُحِبَّكَ اللَّهُ", "Yuhibbakallāh", "Niscaya Allah mencintaimu"),
                    WordTranslation("فِي أَيْدِي النَّاسِ", "Fī aidin-nās", "Pada apa di tangan manusia"),
                    WordTranslation("يُحِبَّكَ النَّاسُ", "Yuhibbakan-nās", "Niscaya manusia mencintaimu")
                ),
                syarahExplanation = "Zuhud bukan berarti meninggalkan harta atau hidup melarat, melainkan tidak menjadikan dunia merajai hati. Tidak tamak dan tidak berharap pada pemberian orang lain menumbuhkan wibawa dan cinta.",
                category = "Zuhud & Ketenangan Hati"
            ),
            Hadith(
                id = 32,
                number = 32,
                titleArabic = "لا ضرر ولا ضرار",
                titleId = "Larangan Membahayakan Diri dan Orang Lain",
                rawi = "HR. Ibnu Majah no. 2341 & Malik dalam Al-Muwaththa' (dari Sa'id bin Sinan)",
                arabicText = "لاَ ضَرَرَ وَلاَ ضِرَارَ.",
                translationId = "Tidak boleh melakukan perbuatan yang membahayakan diri sendiri dan tidak boleh membahayakan orang lain.",
                words = listOf(
                    WordTranslation("لاَ ضَرَرَ", "Lā dharara", "Tidak boleh ada bahaya"),
                    WordTranslation("وَلاَ ضِرَارَ", "Wa lā dhirār", "Dan tidak boleh saling membalas bahaya")
                ),
                syarahExplanation = "Kaidah fiqhiyyah agung yang mencakup muamalah, kesehatan, perlindungan lingkungan, dan tata tertib bertetangga. Setiap aktivitas yang membawa madharat bagi orang lain wajib dicegah.",
                category = "Kaidah Fiqih & Keselamatan"
            ),
            Hadith(
                id = 33,
                number = 33,
                titleArabic = "البينة على المدعي واليمين على من أنكر",
                titleId = "Bukti Bagi Penuduh dan Sumpah Bagi yang Mengingkari",
                rawi = "HR. Al-Baihaqi no. 21201 (dari Ibnu Abbas)",
                arabicText = "لَوْ يُعْطَى النَّاسُ بِدَعْوَاهُمْ لاَدَّعَى رِجَالٌ أَمْوَالَ قَوْمٍ وَدِمَاءَهُمْ، لَكِنَّ الْبَيِّنَةَ عَلَى الْمُدَّعِي، وَالْيَمِيْنَ عَلَى مَنْ أَنْكَرَ.",
                translationId = "Sekiranya setiap manusia diberikan tuntutan hanya berdasarkan pengakuannya semata, niscaya orang-orang akan menuntut harta dan darah orang lain. Akan tetapi bukti wajib didatangkan oleh penuduh, dan sumpah wajib bagi yang mengingkari tuduhan.",
                words = listOf(
                    WordTranslation("بِدَعْوَاهُمْ", "Bi da'wāhum", "Dengan klaim tuduhan mereka"),
                    WordTranslation("الْبَيِّنَةَ", "Al-Bayyinata", "Bukti saksi yang otentik"),
                    WordTranslation("عَلَى الْمُدَّعِي", "'Alal-mudda'ī", "Atas penuduh penggugat"),
                    WordTranslation("الْيَمِيْنَ", "Al-Yamīna", "Sumpah atas nama Allah"),
                    WordTranslation("مَنْ أَنْكَرَ", "Man ankara", "Bagi yang menyangkal")
                ),
                syarahExplanation = "Prinsip hukum peradilan Islam (praduga tak bersalah). Melindungi kehormatan dan hak kepemilikan masyarakat dari tuduhan fitnah tanpa bukti valid.",
                category = "Peradilan & Pembuktian"
            ),
            Hadith(
                id = 34,
                number = 34,
                titleArabic = "مراتب إنكار المنكر",
                titleId = "Tingkatan Mengubah Kemungkaran",
                rawi = "HR. Muslim no. 49 (dari Abu Sa'id Al-Khudri)",
                arabicText = "مَنْ رَأَى مِنْكُمْ مُنْكَرًا فَلْيُغَيِّرْهُ بِيَدِهِ، فَإِنْ لَمْ يَسْتَطِعْ فَبِلِسَانِهِ، فَإِنْ لَمْ يَسْتَطِعْ فَبِقَلْبِهِ، وَذَلِكَ أَضْعَفُ الإِيْمَانِ.",
                translationId = "Barangsiapa di antara kalian melihat suatu kemungkaran, hendaklah ia mengubahnya dengan tangannya (kekuasaan/tindakan), jika tidak mampu maka dengan lisannya (nasihat/teguran), jika tidak mampu maka dengan hatinya (menolak & membencinya), dan itu adalah selemah-lemah iman.",
                words = listOf(
                    WordTranslation("مَنْ رَأَى", "Man ra'ā", "Barangsiapa melihat"),
                    WordTranslation("مُنْكَرًا", "Munkaran", "Kemungkaran maksiat"),
                    WordTranslation("فَلْيُغَيِّرْهُ بِيَدِهِ", "Falyughayyir-hu biyadih", "Ubah dengan tangannya"),
                    WordTranslation("فَبِلِسَانِهِ", "Fa bi lisānih", "Maka dengan lisannya"),
                    WordTranslation("فَبِقَلْبِهِ", "Fa bi qalbih", "Maka dengan hatinya"),
                    WordTranslation("أَضْعَفُ الإِيْمَانِ", "Adh'aful-īmān", "Selemah-lemahnya iman")
                ),
                syarahExplanation = "Metodologi amar ma'ruf nahi munkar yang bijak sesuai otoritas: tangan bagi penguasa/pemimpin, lisan bagi para pendidik dan ulama, serta hati bagi orang awam.",
                category = "Nahi Munkar & Keberanian"
            ),
            Hadith(
                id = 35,
                number = 35,
                titleArabic = "أخوة الإسلام وحقوق المسلم",
                titleId = "Persaudaraan Islam dan Larangan Kedengkian",
                rawi = "HR. Muslim no. 2564 (dari Abu Hurairah)",
                arabicText = "لاَ تَحَاسَدُوا، وَلاَ تَنَاجَشُوا، وَلاَ تَبَاغَضُوا، وَلاَ تَدَابَرُوا، وَلاَ يَبِعْ بَعْضُكُمْ عَلَى بَيْعِ بَعْضٍ، وَكُونُوا عِبَادَ اللَّهِ إِخْوَانًا، الْمُسْلِمُ أَخُو الْمُسْلِمِ لاَ يَظْلِمُهُ وَلاَ يَخْذُلُهُ وَلاَ يَكْذِبُهُ وَلاَ يَحْقِرُهُ، التَّقْوَى هَاهُنَا - وَيُشِيرُ إِلَى صَدْرِهِ ثَلاَثَ مَرَّاتٍ - بِحَسْبِ امْرِئٍ مِنَ الشَّرِّ أَنْ يَحْقِرَ أَخَاهُ الْمُسْلِمَ.",
                translationId = "Janganlah kalian saling mendengki, jangan saling menipu harga beli, jangan saling membenci, jangan saling membelakangi (memutus silaturahim), jangan sebagian kalian membeli atas transaksi saudaranya, dan jadilah kalian hamba-hamba Allah yang bersaudara. Seorang muslim adalah saudara bagi muslim lainnya; tidak boleh menzaliminya, tidak boleh menelantarkannya, tidak boleh membohonginya, dan tidak boleh meremehkannya. Taqwa itu di sini — sambil menunjuk dadanya tiga kali — cukuplah seseorang berdosa jika ia meremehkan saudaranya sesama muslim.",
                words = listOf(
                    WordTranslation("لاَ تَحَاسَدُوا", "Lā tahāsadū", "Jangan saling dengki"),
                    WordTranslation("لاَ تَبَاغَضُوا", "Lā tabāghadhū", "Jangan saling benci"),
                    WordTranslation("إِخْوَانًا", "Ikhwānā", "Bersaudara akrab"),
                    WordTranslation("الْمُسْلِمُ أَخُو الْمُسْلِمِ", "Al-muslimu akhul-muslim", "Muslim saudara bagi muslim"),
                    WordTranslation("التَّقْوَى هَاهُنَا", "At-taqwā hāhunā", "Taqwa itu di dalam dada")
                ),
                syarahExplanation = "Piagam persaudaraan muslim yang melarang segala penyakit hati yang meretakkan persatuan umat. Sikap merendahkan sesama hamba Allah adalah kesombongan nyata.",
                category = "Ukhuwah & Anti-Rasisme"
            ),
            Hadith(
                id = 36,
                number = 36,
                titleArabic = "قضاء حوائج المسلمين وفضل مجالس العلم",
                titleId = "Membantu Kesulitan Sesama dan Keutamaan Majelis Ilmu",
                rawi = "HR. Muslim no. 2699 (dari Abu Hurairah)",
                arabicText = "مَنْ نَفَّسَ عَنْ مُؤْمِنٍ كُرْبَةً مِنْ كُرَبِ الدُّنْيَا نَفَّسَ اللَّهُ عَنْهُ كُرْبَةً مِنْ كُرَبِ يَوْمِ الْقِيَامَةِ، وَمَنْ يَسَّرَ عَلَى مُعْسِرٍ يَسَّرَ اللَّهُ عَلَيْهِ فِي الدُّنْيَا وَالآخِرَةِ، وَمَنْ سَتَرَ مُسْلِمًا سَتَرَهُ اللَّهُ فِي الدُّنْيَا وَالآخِرَةِ، وَاللَّهُ فِي عَوْنِ الْعَبْدِ مَا كَانَ الْعَبْدُ فِي عَوْنِ أَخِيهِ، وَمَنْ سَلَكَ طَرِيقًا يَلْتَمِسُ فِيهِ عِلْمًا سَهَّلَ اللَّهُ لَهُ بِهِ طَرِيقًا إِلَى الْجَنَّةِ...",
                translationId = "Barangsiapa melapangkan satu kesulitan seorang mukmin dari kesulitan dunia, Allah akan melapangkan darinya satu kesulitan di hari kiamat. Barangsiapa memudahkan orang yang dalam kesempitan, Allah akan memudahkan urusannya di dunia dan akhirat. Barangsiapa menutupi aib seorang muslim, Allah akan menutupi aibnya di dunia dan akhirat. Allah senantiasa menolong hamba-Nya selama hamba itu menolong saudaranya. Barangsiapa menempuh jalan menuntut ilmu, Allah mudahkan baginya jalan menuju surga...",
                words = listOf(
                    WordTranslation("مَنْ نَفَّسَ", "Man naffasa", "Barangsiapa melapangkan"),
                    WordTranslation("كُرْبَةً", "Kurbatan", "Kesusahan kesulitan"),
                    WordTranslation("مَنْ سَتَرَ مُسْلِمًا", "Man satara muslimā", "Siapa menutupi aib muslim"),
                    WordTranslation("وَاللَّهُ فِي عَوْنِ الْعَبْدِ", "Wallāhu fī 'awnil-'abd", "Allah selalu menolong hamba"),
                    WordTranslation("طَرِيقًا إِلَى الْجَنَّةِ", "Tharīqan ilal-jannah", "Jalan mulus menuju surga")
                ),
                syarahExplanation = "Motivasi agung untuk saling tolong-menolong, solidaritas sosial, menjaga kehormatan saudara dari aib, serta giat belajar menuntut ilmu agama di majelis-majelis kajian.",
                category = "Solidaritas & Majelis Ilmu"
            ),
            Hadith(
                id = 37,
                number = 37,
                titleArabic = "سعة فضل الله وكتابة الحسنات والسيئات",
                titleId = "Keluasan Karunia Allah dalam Mencatat Kebaikan",
                rawi = "HR. Bukhari no. 6491 & Muslim no. 131 (dari Ibnu Abbas)",
                arabicText = "إِنَّ اللَّهَ كَتَبَ الْحَسَنَاتِ وَالسَّيِّئَاتِ ثُمَّ بَيَّنَ ذَلِكَ: فَمَنْ هَمَّ بِحَسَنَةٍ فَلَمْ يَعْمَلْهَا كَتَبَهَا اللَّهُ عِنْدَهُ حَسَنَةً كَامِلَةً، وَإِنْ هَمَّ بِهَا فَعَمِلَهَا كَتَبَهَا اللَّهُ عِنْدَهُ عَشْرَ حَسَنَاتٍ إِلَى سَبْعِمِائَةِ ضِعْفٍ إِلَى أَضْعَافٍ كَثِيرَةٍ...",
                translationId = "Sesungguhnya Allah menetapkan kebaikan dan keburukan kemudian menjelaskannya: Barangsiapa berniat melakukan suatu kebaikan lalu belum melaksanakannya, Allah mencatat di sisi-Nya sebagai satu kebaikan sempurna. Jika ia berniat lalu mengerjakannya, Allah mencatat di sisi-Nya sepuluh kebaikan hingga tujuh ratus kali lipat bahkan kelipatan yang sangat banyak...",
                words = listOf(
                    WordTranslation("كَتَبَ الْحَسَنَاتِ", "Katabal-hasanāt", "Menetapkan kebaikan"),
                    WordTranslation("فَمَنْ هَمَّ", "Faman hamma", "Siapa berkeinginan kuat"),
                    WordTranslation("حَسَنَةً كَامِلَةً", "Hasanatan kāmilah", "Satu pahala sempurna"),
                    WordTranslation("عَشْرَ حَسَنَاتٍ", "'Asyra hasanāt", "Sepuluh kebaikan berlipat")
                ),
                syarahExplanation = "Rahmat Allah mendahului murka-Nya. Berniat baik saja sudah menuai pahala, sementara niat buruk yang belum dikerjakan tidak dicatat sebagai dosa bahkan diberi pahala jika ditinggalkan karena Allah.",
                category = "Rahmat & Pahala Berlipat"
            ),
            Hadith(
                id = 38,
                number = 38,
                titleArabic = "معاداة أولياء الله والتقرب إليه بالنوافل",
                titleId = "Wali-Wali Allah dan Mendekatkan Diri Lewat Ibadah Sunnah",
                rawi = "HR. Bukhari no. 6502 (dari Abu Hurairah)",
                arabicText = "إِنَّ اللَّهَ قَالَ: مَنْ عَادَى لِي وَلِيًّا فَقَدْ آذَنْتُهُ بِالْحَرْبِ، وَمَا تَقَرَّبَ إِلَيَّ عَبْدِي بِشَيْءٍ أَحَبَّ إِلَيَّ مِمَّا افْتَرَضْتُ عَلَيْهِ، وَمَا يَزَالُ عَبْدِي يَتَقَرَّبُ إِلَيَّ بِالنَّوَافِلِ حَتَّى أُحِبَّهُ، فَإِذَا أَحْبَبْتُهُ كُنْتُ سَمْعَهُ الَّذِي يَسْمَعُ بِهِ، وَبَصَرَهُ الَّذِي يُبْصِرُ بِهِ...",
                translationId = "Sesungguhnya Allah berfirman: 'Barangsiapa memusuhi wali-Ku (kekasih-Ku), maka Aku telah umumkan perang terhadapnya. Tidaklah hamba-Ku mendekatkan diri kepada-Ku dengan sesuatu yang lebih Kucintai daripada apa yang Kuwajibkan atasnya. Dan hamba-Ku senantiasa mendekat kepada-Ku dengan amalan-amalan sunnah hingga Aku mencintainya. Apabila Aku telah mencintainya, jadilah Aku pendengarannya yang ia gunakan untuk mendengar, dan penglihatannya yang ia gunakan untuk melihat...'",
                words = listOf(
                    WordTranslation("مَنْ عَادَى لِي وَلِيًّا", "Man 'ādā lī waliyyā", "Siapa memusuhi wali-Ku"),
                    WordTranslation("آذَنْتُهُ بِالْحَرْبِ", "Ādzantuhū bil-harb", "Aku nyatakan perang padanya"),
                    WordTranslation("بِالنَّوَافِلِ", "Bin-nawāfil", "Dengan amalan sunnah"),
                    WordTranslation("حَتَّى أُحِبَّهُ", "Hattā uhibbah", "Hingga Aku mencintainya")
                ),
                syarahExplanation = "Tingkatan spiritual tertinggi di sisi Allah dicapai melalui kesetiaan pada kewajiban fardhu yang disempurnakan dengan konsistensi amalan nawafil (sunnah).",
                category = "Wali Allah & Taqarrub"
            ),
            Hadith(
                id = 39,
                number = 39,
                titleArabic = "التجاوز عن الخطأ والنسيان والإكراه",
                titleId = "Dimaafkannya Kekeliruan, Lupa, dan Pemaksaan",
                rawi = "HR. Ibnu Majah no. 2045 & Al-Baihaqi (dari Ibnu Abbas)",
                arabicText = "إِنَّ اللَّهَ تَجَاوَزَ لِي عَنْ أُمَّتِي: الْخَطَأَ، وَالنِّسْيَانَ، وَمَا اسْتُكْرِهُوا عَلَيْهِ.",
                translationId = "Sesungguhnya Allah memaafkan dari umatku: kekeliruan (ketidaksengajaan), lupa, dan apa yang dipaksakan kepada mereka.",
                words = listOf(
                    WordTranslation("تَجَاوَزَ لِي", "Tajāwaza lī", "Memaafkan untukku"),
                    WordTranslation("عَنْ أُمَّتِي", "'An ummatī", "Dari umatku"),
                    WordTranslation("الْخَطَأَ", "Al-khatha'", "Kesalahan tak sengaja"),
                    WordTranslation("وَالنِّسْيَانَ", "Wan-nisyān", "Dan karena lupa"),
                    WordTranslation("مَا اسْتُكْرِهُوا", "Mastukrihū 'alaih", "Yang dipaksakan padanya")
                ),
                syarahExplanation = "Bentuk kasih sayang syariat yang tidak membebani hamba di luar kehendak bebasnya. Orang yang keliru atau lupa tidak memikul dosa syar'i.",
                category = "Keringanan Syariat"
            ),
            Hadith(
                id = 40,
                number = 40,
                titleArabic = "كن في الدنيا كأنك غريب",
                titleId = "Hiduplah di Dunia Seperti Orang Asing atau Pengembara",
                rawi = "HR. Bukhari no. 6416 (dari Abdullah bin Umar)",
                arabicText = "أَخَذَ رَسُولُ اللَّهِ صلى الله عليه وسلم بِمَنْكِبِي فَقَالَ: كُنْ فِي الدُّنْيَا كَأَنَّكَ غَرِيبٌ أَوْ عَابِرُ سَبِيلٍ. وَكَانَ ابْنُ عُمَرَ يَقُولُ: إِذَا أَمْسَيْتَ فَلاَ تَنْتَظِرِ الصَّبَاحَ، وَإِذَا أَصْبَحْتَ فَلاَ تَنْتَظِرِ الْمَسَاءَ، وَخُذْ مِنْ صِحَّتِكَ لِمَرَضِكَ، وَمِنْ حَيَاتِكَ لِمَوْتِكَ.",
                translationId = "Rasulullah memegang pundakku lalu bersabda: 'Jadilah engkau di dunia ini seakan-akan orang asing atau pengembara yang sekadar menyeberang jalan.' Dan Ibnu Umar berkata: 'Jika engkau berada di sore hari jangan menunggu pagi, dan jika di pagi hari jangan menunggu sore. Gunakanlah waktu sehatmu sebelum sakitmu, dan masa hidupmu sebelum ajalmu.'",
                words = listOf(
                    WordTranslation("كُنْ فِي الدُّنْيَا", "Kun fid-dunyā", "Jadilah di dunia"),
                    WordTranslation("كَأَنَّكَ غَرِيبٌ", "Ka-annaka gharīb", "Seakan engkau orang asing"),
                    WordTranslation("عَابِرُ سَبِيلٍ", "'Ābiru sabīl", "Pengembara menyeberang jalan"),
                    WordTranslation("مِنْ صِحَّتِكَ لِمَرَضِكَ", "Min shihhatika li maradhik", "Dari sehatmu sebelum sakitmu")
                ),
                syarahExplanation = "Pengingat singkatnya kehidupan duniawi. Orang beriman menyiapkan bekal abadi akhirat dan tidak tertipu oleh angan-angan panjang (thulul amal).",
                category = "Pengingat Kematian & Zuhud"
            ),
            Hadith(
                id = 41,
                number = 41,
                titleArabic = "اتباع هوى النفس لما جاء به النبي",
                titleId = "Menundukkan Hawa Nafsu di Bawah Ajaran Rasul",
                rawi = "HR. Al-Baghawi dalam Syarhus Sunnah (dari Abu Muhammad Abdullah bin Amr)",
                arabicText = "لاَ يُؤْمِنُ أَحَدُكُمْ حَتَّى يَكُونَ هَوَاهُ تَبَعًا لِمَا جِئْتُ بِهِ.",
                translationId = "Tidak sempurna iman salah seorang dari kalian sampai hawa nafsunya tunduk mengikuti apa yang aku bawa (ajaran syariat wahyu).",
                words = listOf(
                    WordTranslation("لاَ يُؤْمِنُ أَحَدُكُمْ", "Lā yu'minu ahadukum", "Tidak sempurna iman seseorang"),
                    WordTranslation("حَتَّى يَكُونَ هَوَاهُ", "Hattā yakūna hawāh", "Hingga kecenderungan nafsunya"),
                    WordTranslation("تَبَعًا", "Taba'an", "Tunduk mengikuti"),
                    WordTranslation("لِمَا جِئْتُ بِهِ", "Limā ji'tu bih", "Apa yang aku bawa")
                ),
                syarahExplanation = "Menundukkan kesenangan pribadi, ego, adat istiadat, dan opini subjektif di bawah titah dan hukum Allah SWT dan Rasul-Nya.",
                category = "Ketaatan & Penundukan Nafsu"
            ),
            Hadith(
                id = 42,
                number = 42,
                titleArabic = "سعة مغفرة الله للتائبين",
                titleId = "Keluasan Ampunan Allah Bagi Hamba yang Memohon",
                rawi = "HR. Tirmidzi no. 3540 (dari Anas bin Malik)",
                arabicText = "يَا ابْنَ آدَمَ إِنَّكَ مَا دَعَوْتَنِي وَرَجَوْتَنِي غَفَرْتُ لَكَ عَلَى مَا كَانَ فِيكَ وَلاَ أُبَالِي، يَا ابْنَ آدَمَ لَوْ بَلَغَتْ ذُنُوبُكَ عَنَانَ السَّمَاءِ ثُمَّ اسْتَغْفَرْتَنِي غَفَرْتُ لَكَ، يَا ابْنَ آدَمَ إِنَّكَ لَوْ أَتَيْتَنِي بِقُرَابِ الأَرْضِ خَطَايَا ثُمَّ لَقِيتَنِي لاَ تُشْرِكُ بِي شَيْئًا لأَتَيْتُكَ بِقُرَابِهَا مَغْفِرَةً.",
                translationId = "Wahai anak Adam, sesungguhnya selama engkau berdoa kepada-Ku dan berharap kepada-Ku, Aku ampuni dosa yang ada padamu dan Aku tidak peduli betapapun besarnya. Wahai anak Adam, sekiranya dosamu mencapai awan di langit kemudian engkau memohon ampun kepada-Ku, niscaya Aku ampuni. Wahai anak Adam, sekiranya engkau datang kepada-Ku membawa dosa sepenuh bumi lalu engkau menemui-Ku tanpa menyekutukan-Ku dengan sesuatu pun, niscaya Aku akan mendatangimu dengan ampunan sepenuh bumi pula.",
                words = listOf(
                    WordTranslation("يَا ابْنَ آدَمَ", "Yabna Ādam", "Wahai anak Adam"),
                    WordTranslation("مَا دَعَوْتَنِي وَرَجَوْتَنِي", "Mā da'awtanī wa rajawtanī", "Selama engkau berdoa & berharap"),
                    WordTranslation("عَنَانَ السَّمَاءِ", "'Anānas-samā'", "Setinggi awan di langit"),
                    WordTranslation("ثُمَّ اسْتَغْفَرْتَنِي", "Tsummas-taghfartanī", "Lalu engkau memohon ampun"),
                    WordTranslation("بِقُرَابِ الأَرْضِ", "Bi qurābil-ardh", "Sepenuh luasnya bumi"),
                    WordTranslation("مَغْفِرَةً", "Maghfirah", "Dengan limpahan ampunan")
                ),
                syarahExplanation = "Penutup agung dari 42 Hadis Arbain An-Nawawi. Membuka harapan seluas-luasnya bagi setiap pendosa untuk bertaubat. Syarat utama keselamatan adalah tauhid murni yang bersih dari syirik.",
                category = "Taubat & Maghfirah"
            )
        )
    }
}
