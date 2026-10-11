package com.example.data.repository

import com.example.data.model.Hadith

data class HadithLangData(
    val titleSu: String,
    val titleEn: String,
    val translationSu: String,
    val translationEn: String,
    val syarahSu: String,
    val syarahEn: String
)

object HadithTranslationsData {
    val data: Map<Int, HadithLangData> = mapOf(
        1 to HadithLangData(
            titleSu = "Niat jeung Kaikhlasan dina Amal",
            titleEn = "Actions are Judged by Intentions",
            translationSu = "Saenyana sakabéh amal gumantung kana niatna, sarta unggal jalma baris meunangkeun naon anu diniatkeun ku manéhna. Saha baé anu hijrahna karana Allah jeung Rasul-Na, mangka hijrahna pikeun Allah jeung Rasul-Na. Jeung saha baé anu hijrahna karana dunya anu hayang dihontalna atawa karana awéwé anu hayang ditikahna, mangka hijrahna nuju kana naon anu jadi tujuan hijrahna.",
            translationEn = "Verily, actions are only by intentions, and every person will only have what they intended. Whoever migrated for Allah and His Messenger, their migration was for Allah and His Messenger. And whoever migrated for worldly gain or to marry a woman, their migration was for that which they migrated to.",
            syarahSu = "Hadis ieu mangrupakeun salasahiji dadasar utama dina agama Islam. Imam Asy-Syafi'i nyarios yén hadis ieu nyangking sapertilu élmu. Niat anu lillahi ta'ala ngabédakeun antara ibadah jeung kabiasaan dunya.",
            syarahEn = "This hadith is one of the greatest foundations of Islam. Imam Ash-Shafi'i stated that it encompasses a third of all religious knowledge. Sincerity of intention distinguishes customary actions from worship and determines acceptance by Allah."
        ),
        2 to HadithLangData(
            titleSu = "Tingkatan Agama: Islam, Iman, jeung Ihsan (Hadis Jibril)",
            titleEn = "Levels of Religion: Islam, Iman, and Ihsan (Hadith of Gabriel)",
            translationSu = "Dina hiji poé nalika urang sadaya nuju calik sasarengan jeung Rasulullah SAW, dumadakan sumping saurang pameget anu anggoanna bodas nyacas sareng rambutna hideung meles, teu aya tanda-tanda jalmi tas musafir... Anjeunna naroskeun perkawis Islam, Iman, sareng Ihsan. Dawuh Kangjeng Nabi: 'Ihsan nyaéta anjeun ibadah ka Allah saolah-olah anjeun ningali Mantenna, lamun anjeun teu ningali Mantenna, saenyana Mantenna ningali anjeun.'",
            translationEn = "One day while we were sitting with the Messenger of Allah (pbuh), a man appeared before us with extremely white clothes and pitch-black hair, showing no signs of travel... He asked about Islam, Iman, and Ihsan. The Prophet said: 'Ihsan is to worship Allah as though you see Him, and if you do not see Him, then surely He sees you.'",
            syarahSu = "Hadis agung anu kasohor minangka Hadis Jibril. Ngawengku sadaya pilar agama: Islam (amal dhohir), Iman (kayakinan batin), sareng Ihsan (penghayatan muraqabah nu pangluhurna).",
            syarahEn = "A comprehensive hadith encompassing the entire structure of the Islamic religion: outward practice (Islam), internal conviction (Iman), and spiritual excellence/mindfulness of the Divine (Ihsan)."
        ),
        3 to HadithLangData(
            titleSu = "Pilar-Pilar Rukun Islam",
            titleEn = "The Five Pillars of Islam",
            translationSu = "Islam diwangun dina lima perkara: nyaksian yén teu aya deui Pangéran anging Allah jeung Muhammad utusan Allah, ngadegkeun shalat, mayar zakat, ibadah haji ka Baitullah, sareng shaum dina sasih Ramadhan.",
            translationEn = "Islam is built upon five pillars: testifying that there is no god but Allah and that Muhammad is the Messenger of Allah, establishing prayer, giving zakat, pilgrimage to the House, and fasting in Ramadan.",
            syarahSu = "Lima pilar ieu mangrupakeun pondasi utama wangunan kaislaman saurang hamba. Saupama salasahiji pondasi rubuh, mangka wangunan agama bakal oyag.",
            syarahEn = "These five foundational deeds form the structural framework of a Muslim's faith and practice in this world and the hereafter."
        ),
        4 to HadithLangData(
            titleSu = "Kajadian Manusa dina Rahim & Takdir Allah",
            titleEn = "Stages of Human Creation and Decree of Destiny",
            translationSu = "Saenyana unggal jalma diantawis anjeun sadaya dikumpulkeun kajadianana dina beuteung ibuna salila opat puluh poé minangka nutfah, teras janten getih kintel, teras janten daging sakepel... Teras diutus malaikat kanggo niupkeun ruh sarta netepkeun opat perkara: rejekina, ajalnya, amalnya, sareng cilaka atanapi bagjana.",
            translationEn = "Verily the creation of each one of you is brought together in his mother's womb for forty days as a drop, then he is a clot or clinging clot for a similar period, then a lump of flesh for a similar period... Then the angel is sent to breathe the spirit into him and decree four matters: his provision, lifespan, deeds, and whether wretched or blessed.",
            syarahSu = "Pangajaran agung perkawis takdir Illahi, tahapan kajadian manusa, sarta parentah supados istiqamah dina amal ibadah dugi ka akhir hayat husnul khatimah.",
            syarahEn = "An essential lesson regarding the stages of embryonic development, divine decree (Qadar), and perseverance in righteousness until one's final breath."
        ),
        5 to HadithLangData(
            titleSu = "Panyaram kana Perkara Anyar dina Agama (Bid'ah)",
            titleEn = "Rejection of Innovations in Religion (Bid'ah)",
            translationSu = "Saha baé anu ngada-ngada perkara anyar dina urusan (agama) kami ieu anu sanés bagian ti dinya, mangka perkara éta ditolak.",
            translationEn = "Whoever introduces into this affair of ours something which does not belong to it, it is rejected.",
            syarahSu = "Patokan agung dina syariat Islam yén sagala ibadah kedah saluyu sareng pituduh Al-Qur'an sareng Sunnah Rasulullah SAW supados ditampi ku Allah.",
            syarahEn = "A foundational principle in Islamic jurisprudence stating that worship must adhere strictly to authentic Prophetic tradition."
        ),
        6 to HadithLangData(
            titleSu = "Perkara Halal, Haram, jeung Syubhat",
            titleEn = "The Halal, the Haram, and Doubtful Matters",
            translationSu = "Saenyana anu halal geus écés sarta anu haram ogé geus écés. Di antara duanana aya perkara-perkara syubhat anu teu dipikanyaho ku seueur jalma. Saha anu ngajaga diri tina syubhat, mangka manéhna geus nyalametkeun agama jeung kahormatanana...",
            translationEn = "Verily that which is lawful is clear and that which is unlawful is clear, and between them are matters that are doubtful which many people do not know. Whoever guards against doubtful matters preserves his religion and his honor...",
            syarahSu = "Kautamaan wara' nyaéta ninggalkeun perkara anu mangmang supados manah tetep beresih tina dosa sareng karuksakan batin.",
            syarahEn = "The virtue of scrupulousness (wara') in avoiding ambiguities in transactions, food, and conduct to protect the purity of one's heart."
        ),
        7 to HadithLangData(
            titleSu = "Agama téh Nyaéta Naséhat (Kaikhlasan)",
            titleEn = "Religion is Sincerity and Good Advice",
            translationSu = "Agama téh nyaéta naséhat. Kami naros: 'Kanggo saha wahai Rasulullah?' Dawuh anjeunna: 'Pikeun Allah, Kitab-Na, Rasul-Na, pamimpin kaum muslimin, sareng pikeun sakabéh umat Islam umumna.'",
            translationEn = "Religion is sincerity (nasihah). We asked: 'To whom, O Messenger of Allah?' He replied: 'To Allah, His Book, His Messenger, and to the leaders of the Muslims and their common folk.'",
            syarahSu = "Naséhat hartosna kamurnian manah, kaikhlasan, sareng kahoyong kasaéan pikeun papada jalma dina sagala widang kahirupan.",
            syarahEn = "Nasihah signifies absolute sincerity of devotion towards God and genuine benevolence towards fellow humanity and society."
        ),
        8 to HadithLangData(
            titleSu = "Kahormatan Getih jeung Harta Muslim",
            titleEn = "The Sanctity of a Muslim's Blood and Wealth",
            translationSu = "Kuring diparéntah pikeun merangan manusa nepi ka maranéhna nyaksian yén euweuh Pangéran anging Allah jeung Muhammad utusan Allah, ngadegkeun shalat, jeung mayar zakat. Lamun maranéhna geus migawé éta, mangka geus kajaga getih jeung hartana...",
            translationEn = "I have been commanded to fight against people until they testify that there is no god but Allah and Muhammad is the Messenger of Allah, establish prayer, and pay zakat. If they do that, they have protected their blood and wealth from me...",
            syarahSu = "Islam ngahargaan pisan kasalametan nyawa sareng harta unggal manusa anu parantos ngucapkeun kalimah syahadat.",
            syarahEn = "The inviolability of human life, dignity, and private property once the declaration of faith and duties are embraced."
        ),
        9 to HadithLangData(
            titleSu = "Ngalaksanakeun Pituduh Sakamampuhna",
            titleEn = "Fulfilling Obligations According to Ability",
            translationSu = "Naon anu kuring larang pikeun anjeun, mangka jauhan ku anjeun. Jeung naon anu kuring paréntahkeun ka anjeun, mangka laksanakeun sagawé sakamampuh anjeun...",
            translationEn = "What I have forbidden you, avoid; and what I have ordered you to do, fulfill as much of it as you are able...",
            syarahSu = "Kagampangan sareng karaharjaan dina syariat Islam: panyaram kedah dijauhan sacara mutlak, sedengkeun paréntah dilakukeun dumasar kamampuh masing-masing.",
            syarahEn = "The balance in Islamic law: prohibitions must be avoided completely, whereas positive commandments are undertaken to the extent of one's capability."
        ),
        10 to HadithLangData(
            titleSu = "Neda Anu Halal Tur Thayyib",
            titleEn = "Consuming Pure and Lawful Sustenance",
            translationSu = "Saenyana Allah Ta'ala Maha Saé, Mantenna henteu nampi anging anu saé... Teras Rasul nyarioskeun jalma anu leumpang jauh, buukna acak-acakan jeung pinuh ku lebu, ngadua namprakkeun panangan ka langit: 'Duh Gusti, Duh Gusti!', sedengkeun tuangeunana haram, leueuteunana haram, anggoanna haram... Kumaha rék diijabah duana?",
            translationEn = "Verily Allah the Exalted is pure and accepts only what is pure... Then he mentioned a man who has traveled on a long journey, disheveled and dusty, who stretches out his hands to the sky: 'O Lord, O Lord!' while his food is unlawful, his drink is unlawful, his clothing is unlawful... How then could his prayer be answered?",
            syarahSu = "Kadaharan anu halal mangrupakeun sarat mutlak supados doa diijabah ku Allah SWT sareng amal ibadah katarima kalayan sampurna.",
            syarahEn = "Lawful, wholesome sustenance is an essential prerequisite for the acceptance of supplications and righteous deeds."
        ),
        11 to HadithLangData(
            titleSu = "Ninggalkeun Perkara Anu Mangmang",
            titleEn = "Abandoning That Which Causes Doubt",
            translationSu = "Tinggalkeun naon-naon anu matak mangmang ka anjeun, pindah kana naon-naon anu henteu matak mangmang ka anjeun.",
            translationEn = "Leave that which makes you doubt for that which does not make you doubt.",
            syarahSu = "Pangajaran wara' sareng katenangan batin: kasaéan ngadatangkeun katengtreman dina manah, sedengkeun dosa ngadatangkeun mamang jeung geumpeur.",
            syarahEn = "True righteousness brings tranquility to the conscience, whereas doubtful and sinful matters agitate the soul."
        ),
        12 to HadithLangData(
            titleSu = "Ninggalkeun Perkara Anu Teu Aya Gunana",
            titleEn = "Leaving That Which Does Not Concern You",
            translationSu = "Salasahiji ciri tina kasampurnaan kaislaman saurang jalma nyaéta manéhna ninggalkeun perkara anu teu aya gunana pikeun dirina.",
            translationEn = "Part of the perfection of a person's Islam is his leaving that which does not concern him.",
            syarahSu = "Pituduh agung dina ngajaga waktos, ucapan, jeung pipikiran tina hal-hal anu hapa tina mangpaat dunya sareng ahérat.",
            syarahEn = "Discipline of speech, thought, and time by discarding futile, irrelevant pursuits that yield neither worldly nor spiritual benefit."
        ),
        13 to HadithLangData(
            titleSu = "Nyaah ka Dulur Siga ka Diri Sorangan",
            titleEn = "Loving for One's Brother What One Loves for Oneself",
            translationSu = "Henteu sampurna iman salasaurang di antara anjeun sadaya nepi ka manéhna micinta pikeun dulurna naon anu dipicinta ku dirina sorangan.",
            translationEn = "None of you truly believes until he loves for his brother what he loves for himself.",
            syarahSu = "Puncak duduluran dina Islam nyaéta ngaleungitkeun rasa hasad jeung sirik, diganti ku kanyaah sarta miharep kasaéan pikeun sakabéh muslim.",
            syarahEn = "The pinnacle of universal brotherhood in faith: eradicating jealousy and desiring goodness and prosperity for others as for oneself."
        ),
        14 to HadithLangData(
            titleSu = "Panyaram Ngocorkeun Getih Muslim",
            titleEn = "Sanctity of Human Life in Islam",
            translationSu = "Henteu halal getih saurang muslim anu nyaksian yén teu aya Pangéran anging Allah jeung kuring utusan Allah, iwal ku salasahiji tina tilu perkara: jalma anu geus nikah tapi migawé zina, nyawa dibales ku nyawa (qishash), jeung jalma anu ninggalkeun agamana (murtad) sarta misahkeun diri ti jama'ah.",
            translationEn = "The blood of a Muslim who testifies that there is no god but Allah and that I am the Messenger of Allah is not lawful except in one of three cases: the married person who commits adultery, a life for a life, and the one who abandons his religion and separates from the community.",
            syarahSu = "Ngajelaskeun kasucian getih saurang muslim sareng aturan qishash dina kaadilan hukum Islam.",
            syarahEn = "Clarifying the supreme sanctity of human life and the precise legal limits governed by legitimate judicial authority."
        ),
        15 to HadithLangData(
            titleSu = "Nyarios Saé atanapi Jempling & Ngamulyakeun Tatangga",
            titleEn = "Good Speech or Silence, and Honoring Neighbors and Guests",
            translationSu = "Saha baé anu iman ka Allah jeung poé akhir, mangka kudu nyarita anu hadé atawa cicing. Jeung saha baé anu iman ka Allah jeung poé akhir, mangka kudu ngamulyakeun tatanggana. Jeung saha baé anu iman ka Allah jeung poé akhir, mangka kudu ngamulyakeun sémahna.",
            translationEn = "Whoever believes in Allah and the Last Day should speak good or remain silent. Whoever believes in Allah and the Last Day should honor his neighbor. And whoever believes in Allah and the Last Day should honor his guest.",
            syarahSu = "Adab lisan anu luhung sareng adab sosial dina masarakat: ngajaga lisan, ngahargaan tatangga, sareng ngabagéakeun sémah kalayan soméah.",
            syarahEn = "Civic ethics rooted in faith: controlling speech, gracious hospitality to visitors, and benevolent coexistence with neighbors."
        ),
        16 to HadithLangData(
            titleSu = "Ulah Bendu (Ulah Babari Ambek)",
            titleEn = "Do Not Become Angry",
            translationSu = "Saurang pameget nyarios ka Kangjeng Nabi SAW: 'Wasiatan abdi.' Dawuh anjeunna: 'Ulah ambek!' Jalma éta ngulang deui pananyana sababaraha kali, Nabi tetep ngadawuh: 'Ulah ambek!'",
            translationEn = "A man said to the Prophet (pbuh): 'Counsel me.' He said: 'Do not become angry.' The man repeated his request several times, and each time the Prophet said: 'Do not become angry.'",
            syarahSu = "Nahan amarah mangrupakeun salasahiji tanda kakiatan rohani anu sajati. Amarah anu henteu kakadalian nyaéta lawang sagala kacilakaan.",
            syarahEn = "Self-restraint over anger is the hallmark of spiritual resilience and emotional maturity, preventing transgression and regret."
        ),
        17 to HadithLangData(
            titleSu = "Migawe Kahadean (Ihsan) dina Sagala Perkara",
            titleEn = "Prescribing Excellence (Ihsan) in All Things",
            translationSu = "Saenyana Allah parantos netepkeun sangkan migawé kahadéan (ihsan) dina sagala perkara. Lamun anjeun maéhan, mangka paéhan kalayan hadé. Lamun anjeun meuncit sasatoan, mangka peuncit kalayan hadé, seukeutkeun peso anjeun sarta genahkeun sato peuncitanana.",
            translationEn = "Verily Allah has prescribed excellence in everything. If you kill, kill well; and if you slaughter, slaughter well. Let each one of you sharpen his blade and relieve his suffering animal.",
            syarahSu = "Rahmat Islam anu jembar nepi ka sasatoan ogé kedah diperlakukeun kalayan welas asih sareng ihsan nalika dipeuncit.",
            syarahEn = "Universal compassion and professional precision in Islam, extending mercy even to animals during slaughter by minimizing pain."
        ),
        18 to HadithLangData(
            titleSu = "Taqwa ka Allah & Akhlak Anu Saé",
            titleEn = "Taqwa of Allah and Nobility of Character",
            translationSu = "Kudu taqwa anjeun ka Allah di mana baé anjeun aya. Susul lampah goréng ku lampah hadé, tangtu kahadéan éta bakal ngahapus kagoréngan. Sarta campur gaul jeung papada manusa ku akhlak anu hadé.",
            translationEn = "Fear Allah wherever you may be; follow up an evil deed with a good one to wipe it out; and treat people with good character.",
            syarahSu = "Tilu konci kasalametan: taqwa dina kaayaan nyepi atanapi ramé, tobat ku migawé amal shalih, sareng budi pekerti anu luhur.",
            syarahEn = "Three golden maxims: constant God-consciousness in private and public, expiating wrongs through righteous actions, and exemplary social conduct."
        ),
        19 to HadithLangData(
            titleSu = "Ngajaga Hak-Hak Allah & Tawakal",
            titleEn = "Guarding Allah's Limits and Relying Upon Him",
            translationSu = "Jaga hak-hak Allah, tangtu Allah bakal ngajaga anjeun. Jaga hak-hak Allah, tangtu anjeun bakal manggihan Mantenna di hareupeun anjeun. Lamun anjeun ménta, ménta ka Allah. Lamun anjeun ménta tulung, ménta tulung ka Allah...",
            translationEn = "Be mindful of Allah, and He will protect you. Be mindful of Allah, and you will find Him in front of you. If you ask, ask of Allah; if you seek help, seek help from Allah...",
            syarahSu = "Wasiat agung ka Ibnu Abbas perkawis kakuatan tauhid, kaimanan kana qada jeung qadar, sarta ulah sieun ku manusa lamun Allah parantos nangtayungan.",
            syarahEn = "Prophetic counsel to Ibn Abbas regarding profound monotheism, absolute reliance upon the Creator, and freedom from fear of created beings."
        ),
        20 to HadithLangData(
            titleSu = "Rumaos Isin Bagian tina Iman",
            titleEn = "Modesty and Shame are Integral to Faith",
            translationSu = "Saenyana salasahiji tina perkara anu dipikanyaho ku manusa tina ucapan para nabi ti baheula nyaéta: 'Lamun anjeun geus euweuh rasa éra, mangka pék gawé sakahayang anjeun!'",
            translationEn = "Verily, among the teachings people have received from the words of the earlier prophethood is: 'If you feel no shame, then do as you please.'",
            syarahSu = "Rasa éra ka Allah sareng ka papada manusa mangrupakeun benteng utama anu nahan jalma tina migawé maksiat jeung lampah tuna budi.",
            syarahEn = "Modesty and moral conscience (Haya') form the inward barrier safeguarding human dignity and protecting society from depravity."
        ),
        21 to HadithLangData(
            titleSu = "Kanceuh Istiqamah dina Iman",
            titleEn = "Steadfastness in Faith (Istiqamah)",
            translationSu = "Kuring nyarios: 'Wahai Rasulullah, carioskeun ka abdi dina urusan Islam hiji ucapan anu abdi henteu kedah naroskeun deui ka salian anjeun.' Dawuh anjeunna: 'Ucapkeun ku anjeun: Kuring iman ka Allah, tuluy kudu istiqamah anjeun!'",
            translationEn = "I said: 'O Messenger of Allah, tell me something in Islam which I will not need to ask anyone other than you.' He said: 'Say: I believe in Allah, and then remain steadfast.'",
            syarahSu = "Karakter jawami'ul kalim: ngahijikeun iman dina jero kalbu sareng tekun istiqamah dina ngalaksanakeun paréntah sapanjang umur.",
            syarahEn = "Succinct wisdom unifying conviction in God with persistent consistency in righteous practice throughout life."
        ),
        22 to HadithLangData(
            titleSu = "Jalan Menuju ka Surga",
            titleEn = "The Straight Path to Paradise",
            translationSu = "Kumaha saur salira, lamun kuring ngerjakeun shalat fardhu, puasa Ramadhan, ngahalalkeun anu halal, ngaharamkeun anu haram, sarta henteu nambahan deui naon-naon kana éta, naha kuring bakal asup ka surga? Rasulullah SAW ngawaler: 'Enya!'",
            translationEn = "Do you think that if I perform the obligatory prayers, fast in Ramadan, treat as lawful that which is lawful, and treat as forbidden that which is forbidden, and do nothing more than that, shall I enter Paradise? He replied: 'Yes.'",
            syarahSu = "Kagampangan Islam pikeun jalma anu ngalaksanakeun kawajiban-kawajiban fardhu kalayan ikhlas sarta ngajauhan perkara haram.",
            syarahEn = "Reassurance that fulfilling primary religious obligations and abstaining from prohibitions guarantees entry to Paradise."
        ),
        23 to HadithLangData(
            titleSu = "Kasucian & Kautamaan Sagala Rupi Amal",
            titleEn = "Purification, Prayer, and Daily Striving",
            translationSu = "Bersuci téh satengah tina iman. Alhamdulillah minuhan timbangan. Subhanallah jeung Alhamdulillah minuhan antara langit jeung bumi. Shalat téh cahaya, sedekah téh bukti, kasabaran téh panyinar, sarta Al-Qur'an téh hujah pikeun anjeun atawa sabalikna ngalawan anjeun...",
            translationEn = "Purity is half of faith. Al-hamdu lillah fills the scale, and subhan Allah and al-hamdu lillah fill what is between heaven and earth. Prayer is a light, charity is a proof, patience is an illumination, and the Qur'an is an argument for you or against you...",
            syarahSu = "Pangajaran jero perkawis bobot dzikir, hakekat shalat, kakuatan sabar, sareng tanggung jawab manusa dina ngamalkeun Al-Qur'an.",
            syarahEn = "Exposition of spiritual purity, the infinite weight of glorifying God, the illuminating guidance of prayer and patience, and the witness of Scripture."
        ),
        24 to HadithLangData(
            titleSu = "Haramna Lampah Zhalim (Hadis Qudsi)",
            titleEn = "The Prohibition of Injustice (Hadith Qudsi)",
            translationSu = "Wahai hamba-hamba Kami, saenyana Kami geus ngaharamkeun kazoliman pikeun Diri Kami sarta Kami ngajadikeun kazoliman haram di antara maranéh, mangka maranéh ulah silih zoliman! Wahai hamba-hamba Kami, sakabéh maranéh téh sasar iwal anu dibéré pituduh ku Kami...",
            translationEn = "O My servants, I have forbidden oppression for Myself and have made it forbidden amongst you, so do not oppress one another! O My servants, all of you are astray except those whom I have guided...",
            syarahSu = "Salah sahiji Hadis Qudsi pangagungna anu netepkeun kaadilan Illahi, kakawasaan Allah anu sampurna, sarta butuhna manusa kana panghampura Mantenna.",
            syarahEn = "Among the most profound sacred traditions (Hadith Qudsi), demonstrating Divine justice, human dependence on God, and the absolute prohibition of oppression."
        ),
        25 to HadithLangData(
            titleSu = "Sedekah Sanés Ngan Saukur Harta",
            titleEn = "Charity is Open to Everyone, Not Only the Wealthy",
            translationSu = "Sababaraha sohabat anu mariskin ngadugikeun ka Nabi SAW: 'Jalma-jalma beunghar meunangkeun pahala anu kacida lobana...' Dawuh anjeunna: 'Naha Allah teu geus nyadiakeun naon-naon anu bisa disedekahkeun ku maranéh? Saenyana unggal tasbih téh sedekah, unggal takbir téh sedekah, unggal tahmid téh sedekah, amar ma'ruf nahi munkar téh sedekah...'",
            translationEn = "Some impoverished companions said to the Prophet: 'The wealthy have monopolized the rewards...' He replied: 'Has not Allah made for you things wherewith to give charity? Every glorification is a charity, every takbir is a charity, every praise is a charity, enjoining good and forbidding evil is a charity...'",
            syarahSu = "Rahmat Islam anu mukakeun kasempetan sodakoh pikeun sakumna umat sanajan teu boga harta dunya.",
            syarahEn = "Democratization of virtue in Islam: physical energy, verbal remembrance, social mediation, and intimate familial affection all constitute charity."
        ),
        26 to HadithLangData(
            titleSu = "Saban Sendi Awak Wajib Disodakohan",
            titleEn = "Charity for Every Joint of the Human Body",
            translationSu = "Unggal sendi manusa wajib disedekahan dina unggal poé di mana panonpoé meleték: anjeun ngadamaikeun dua jalma kalayan adil mangrupa sedekah, anjeun nulungan jalma kana tumpakanana mangrupa sedekah, ucapan anu hadé téh sedekah, unggal léngkah nuju shalat téh sedekah, jeung nyingkirkeun cucuk tina jalan téh sedekah.",
            translationEn = "Every joint of a person must perform a charity on each day that the sun rises: judging justly between two people is a charity, helping a man with his mount is a charity, a good word is a charity, every step taken to prayer is a charity, and removing harm from the road is a charity.",
            syarahSu = "Syukur kana nikmat kaséhatan awak ku cara ngabagikeun mangpaat ka lingkungan sakurilingna saban dinten.",
            syarahEn = "Gratitude for the blessing of bodily faculties manifested through active civic responsibility, public courtesy, and reconciliation."
        ),
        27 to HadithLangData(
            titleSu = "Wangenan Kahadean jeung Dosa",
            titleEn = "The Essence of Righteousness and Sin",
            translationSu = "Kasaéan téh nyaéta akhlak anu hadé, sedengkeun dosa téh naon-naon anu matak henteu genah dina jero dada anjeun sarta anjeun henteu resep lamun éta hal kanyahoan ku batur.",
            translationEn = "Righteousness is good character, and sin is that which wavers in your heart and which you would dislike for people to find out about.",
            syarahSu = "Fitrah manusa anu beresih baris ngarasa tenang ku kahadéan sarta ngarasa geumpeur mamang ku dosa maksiat.",
            syarahEn = "The moral compass inherent in human fitrah, recognizing intrinsic virtue by serene conviction and sin by inner agitation."
        ),
        28 to HadithLangData(
            titleSu = "Gegel Pageuh Kana Sunnah Rasul",
            titleEn = "Adhering Firmly to the Prophetic Tradition",
            translationSu = "Kuring wasiatkeun ka anjeun sadaya sangkan taqwa ka Allah, ngadéngé jeung taat sanajan dipingpin ku budak Habsyi... Sing saha anu hirup saenggeus kuring baris ningali seueur pacogrégan, mangka kudu nyepeng pageuh kana sunnah kuring jeung sunnah Khulafaur Rasyidin, gegel ku huntu gerham anjeun!",
            translationEn = "I counsel you to have taqwa of Allah, and to listen and obey even if a slave is placed in authority over you... Whoever among you lives after me will see great disagreement; so adhere strictly to my Sunnah and the sunnah of the rightly guided Caliphs; bite onto it with your molars!",
            syarahSu = "Pépéling agung tina wasiat pamungkas Rasulullah SAW dina mayunan jaman anu pinuh ku pacaduan jeung fitnah.",
            syarahEn = "Poignant farewell sermon urging steadfast unity, obedience to legitimate leadership, and firm adherence to pristine orthodoxy during epochs of discord."
        ),
        29 to HadithLangData(
            titleSu = "Panto Kahadean & Ngajaga Lisan",
            titleEn = "Doors of Goodness and Restraining the Tongue",
            translationSu = "Mu'adz bin Jabal naros perkawis amal anu ngalebetkeun ka surga. Rasul ngawaler: shaum téh tameng, sedekah mareuman kasalahan, shalat peuting... Teras dawuh anjeunna: 'Naha anjeun hayang dibéré nyaho konci tina sagala perkara éta?' Anjeunna nyepeng lisanana sarta ngadawuh: 'Jaga ieu ku anjeun!'",
            translationEn = "Mu'adh ibn Jabal asked about deeds admitting one to Paradise. The Prophet spoke of fasting as a shield, charity extinguishing sin, night prayers... Then he asked: 'Shall I not tell you of the foundation of all that?' He seized his tongue and said: 'Restrain this!'",
            syarahSu = "Lisan mangrupakeun sabab utama anu nyered manusa kana naraka lamun henteu dijaga tina ghibah, fitnah, sareng bohong.",
            syarahEn = "The tongue is the decisive governor of human destiny; without guarding speech, all outward devotional practices are compromised."
        ),
        30 to HadithLangData(
            titleSu = "Watesan-Watesan Hukum Allah",
            titleEn = "The Boundaries Prescribed by Allah",
            translationSu = "Saenyana Allah Ta'ala parantos netepkeun kawajiban-kawajiban, mangka ulah disapirakeun ku anjeun; parantos netepkeun watesan-watesan hukum, mangka ulah dilanggar ku anjeun; parantos ngaharamkeun perkara-perkara, mangka ulah ditincak ku anjeun; sarta Mantenna jempé tina sababaraha perkara minangka rahmat lain karana poho, mangka ulah dipaluruh kalayan kaleuleuwihi.",
            translationEn = "Verily Allah the Exalted has laid down obligations, so do not neglect them; He has set limits, so do not exceed them; He has forbidden things, so do not violate them; and He has remained silent about things out of mercy for you, not forgetfulness, so do not search into them.",
            syarahSu = "Kaidah agung dina ngartos struktur syariat Islam: aya fardhu, aya watesan, aya panyaram, sareng aya rohangan kalugaan rahmat.",
            syarahEn = "A comprehensive taxonomy of divine legislation: commands, limits, prohibitions, and areas of concession spared from burdensome scrutiny."
        ),
        31 to HadithLangData(
            titleSu = "Hakekat Zuhud di Dunya",
            titleEn = "The Reality and Virtue of Zuhud (Asceticism)",
            translationSu = "Saurang pameget sumping naros: 'Wahai Rasulullah, tuduhkeun ka kuring hiji amal anu lamun ku kuring dilakonan, kuring baris dipikacinta ku Allah jeung dipikacinta ku manusa.' Dawuh anjeunna: 'Zuhud anjeun kana dunya, tangtu Allah baris mikacinta ka anjeun; jeung zuhud anjeun tina naon anu aya di leungeun manusa, tangtu manusa baris mikacinta ka anjeun.'",
            translationEn = "A man came and said: 'O Messenger of Allah, direct me to an action which, if I do it, Allah will love me and people will love me.' He said: 'Be detached from the world and Allah will love you; and be detached from what people possess and people will love you.'",
            syarahSu = "Zuhud sanés hartina teu boga nanaon, tapi manah henteu kabeungkeut ku dunya sarta henteu sarakah kana harta hak batur.",
            syarahEn = "True spiritual detachment (Zuhud) frees the soul from materialistic slavery and harmonizes interpersonal relations by eliminating greed."
        ),
        32 to HadithLangData(
            titleSu = "Ulah Madorotkeun Diri & Batur (La Dharara)",
            titleEn = "No Harm and No Counter-Harm (La Darar wa La Dirar)",
            translationSu = "Henteu meunang ngadatangkeun bahaya (kamadorotan) pikeun diri sorangan, sarta henteu meunang males ngabahayakeun ka batur.",
            translationEn = "There should be neither harming nor reciprocating harm.",
            syarahSu = "Kaidah fiqih emas dina Islam anu ngalarang sagala rupa tindakan anu ngarugikeun kasehatan, lingkungan, sareng masarakat umum.",
            syarahEn = "One of the five overarching legal maxims in Islamic jurisprudence, strictly prohibiting the initiation or reciprocal escalation of harm."
        ),
        33 to HadithLangData(
            titleSu = "Bukti dina Perkara Gugatan Hukum",
            titleEn = "The Burden of Proof and the Oath in Litigation",
            translationSu = "Saupama unggal jalma dibéré hak ngan saukur dumasar kana pangakuanna, tangtu bakal loba jalma anu nungtut getih jeung harta kaum lianna. Tapi bukti wajib ditepikeun ku jalma anu ngagugat, sedengkeun sumpah wajib pikeun jalma anu ngabantah.",
            translationEn = "Were people to be given according to their claims, men would claim the blood and property of others; but the burden of proof is upon the claimant, and the oath is upon the one who denies.",
            syarahSu = "Pondasi kaadilan hukum Islam anu ngajagi hak-hak masarakat sangkan henteu aya anu dizoliman ku tuduhan palsu.",
            syarahEn = "The cornerstone of procedural justice and evidence in Islamic jurisprudence, protecting innocence and establishing rigorous evidential standards."
        ),
        34 to HadithLangData(
            titleSu = "Ngarobih Kamungkaran",
            titleEn = "Correcting Evil Actions by Hand, Tongue, or Heart",
            translationSu = "Saha baé di antara anjeun sadaya anu ningali kamungkaran, mangka kudu ngarobah éta ku leungeunna (kakawasaan); lamun teu sanggup, mangka ku lisanana; lamun teu sanggup kénéh, mangka ku haténa (ngageuleuh), sarta éta téh tingkatan iman anu panghengkérna.",
            translationEn = "Whosoever of you sees an evil action, let him change it with his hand; and if he is not able to do so, then with his tongue; and if he is not able to do so, then with his heart, and that is the weakest of faith.",
            syarahSu = "Kewajiban amar ma'ruf nahi munkar dumasar tingkatan kamampuh masing-masing kalayan hikmah sarta ulah nimbulkeun kamadorotan nu leuwih gede.",
            syarahEn = "Graduated societal and personal obligation to resist moral and civic corruption constructively in accordance with one's capacity and authority."
        ),
        35 to HadithLangData(
            titleSu = "Sadulur Islam & Ulah Hasad",
            titleEn = "Brotherhood in Islam and Prohibition of Envy",
            translationSu = "Ulah silih hasad, ulah silih najasy (nipu harga), ulah silih geuleuh, ulah silih nukangan, jeung ulah meuli barang anu keur ditawar ku batur. Jadilah anjeun hamba-hamba Allah anu duduluran. Saurang muslim téh dulur pikeun muslim lianna: ulah ngazalimannana, ulah ngerakeunana, jeung ulah ngahinakeunana. Taqwa téh aya di dieu (bari nunjuk kana dadana tilu kali)...",
            translationEn = "Do not envy one another, do not artificially inflate prices, do not hate one another, do not turn away from one another, and do not undercut one another in trade. Be, O servants of Allah, brothers! A Muslim is the brother of a Muslim: he does not oppress him, nor abandon him, nor lie to him, nor despise him. Taqwa is here (pointing to his chest three times)...",
            syarahSu = "Piagam duduluran universal Islam anu ngabasmi rupa-rupa bibit panyakit haté, hasad, sareng perpecahan di kalangan masarakat.",
            syarahEn = "The manifesto of Islamic solidarity, systematically eradicating social vices such as commercial exploitation, malicious gossip, hatred, and arrogance."
        ),
        36 to HadithLangData(
            titleSu = "Nulung Kasesah Dulur Muslim",
            titleEn = "Relieving the Distress of a Believer and Seeking Knowledge",
            translationSu = "Saha baé anu ngaleungitkeun salasahiji kasusah dunya saurang mukmin, Allah baris ngaleungitkeun salasahiji kasusahna dina poé kiamat. Saha anu ngagampangkeun jalma anu meunang kasusahan, Allah baris ngagampangkeun urusanana di dunya jeung ahérat... Allah baris salawasna nulungan saurang hamba salila éta hamba nulungan dulurna...",
            translationEn = "Whoever relieves a believer of a distress of this world, Allah will relieve him of a distress of the Day of Resurrection. Whoever eases the plight of an indebted person, Allah will ease his plight in this world and the hereafter... Allah continues to help His servant so long as the servant is helping his brother...",
            syarahSu = "Kautamaan ageung dina silih tulungan, nutupan aib papada muslim, sarta ngambah jalan thalabul 'ilmi anu digampangkeun jalanna ka surga.",
            syarahEn = "The immense virtue of mutual aid, concealing the faults of fellow believers, and embarking upon the noble pursuit of sacred knowledge."
        ),
        37 to HadithLangData(
            titleSu = "Kahadean & Kagorengan Anu Ditikel-tikel",
            titleEn = "Multiplication of Good Deeds and Divine Grace",
            translationSu = "Saenyana Allah parantos netepkeun kahadéan jeung kagoréngan, teras ngajelaskeunana: saha anu boga niat migawé kahadéan tapi teu tulus migawéna, Allah nyatet pikeun manéhna hiji kahadéan anu sampurna. Lamun manéhna boga niat sarta migawéna, Allah nyatet pikeun manéhna sapuluh nepi ka tujuh ratus tikel kahadéan... Sedengkeun lamun boga niat jahat tapi teu dipigawé, dicatet hiji kahadéan...",
            translationEn = "Verily Allah has written down the good deeds and the bad deeds, then He clarified how: whoever intends to perform a good deed and does not do it, Allah records it with Himself as a complete good deed; and if he intends it and does it, Allah records it as ten to seven hundred times or more... But if he intends an evil deed and does not do it, Allah records it as a full good deed...",
            syarahSu = "Kajembaran rahmat sareng kaasih Allah SWT anu henteu aya watesna dina maparin ganjaran kasaéan ka hamba-hamba-Na.",
            syarahEn = "The boundless mercy and grace of God in vastly multiplying the reward for righteous intentions and restraining retribution for sinful urges."
        ),
        38 to HadithLangData(
            titleSu = "Wali Allah & Katresna Illahi (Hadis Qudsi)",
            titleEn = "The Allies of Allah and Drawing Near Through Devotion",
            translationSu = "Saha anu mumusuhan jeung wali (kekasih) Kami, mangka Kami geus ngumumkeun perang ka manéhna. Jeung henteu aya amal anu leuwih dipikacinta ku Kami pikeun hamba Kami ngadeukeutkeun diri ka Kami salian ti naon anu geus difardhukeun ku Kami ka manéhna. Jeung hamba Kami baris tuluy ngadeukeutkeun diri ku amalan sunnah nepi ka Kami mikacinta ka manéhna...",
            translationEn = "Whosoever shows enmity to a friend of Mine, I have declared war upon him. My servant does not draw near to Me with anything more loved by Me than the duties I have enjoined upon him, and My servant continues to draw near to Me with voluntary acts until I love him...",
            syarahSu = "Tingkatan kasucian drajat kaislaman: tina ngamalkeun kawajiban fardhu dugi ka amalan sunnah anu ngalahirkeun katresna Allah dugi ka paningal sareng leungeunna dijagi ku Mantenna.",
            syarahEn = "The pinnacle of spiritual ascension and closeness to God: commencing with obligatory devotions and culminating in voluntary devotions that merit divine love and guardianship."
        ),
        39 to HadithLangData(
            titleSu = "Panghampura tina Kasalahan, Hilap, & Kapaksa",
            titleEn = "Forgiveness for Mistakes, Forgetfulness, and Coercion",
            translationSu = "Saenyana Allah parantos ngahampura pikeun umat kuring kasalahan (anu henteu dihaja), poho (hilap), sareng naon-naon anu dipaksakeun ka maranéhna.",
            translationEn = "Verily Allah has pardoned for my nation their mistakes, their forgetfulness, and what they are coerced into doing.",
            syarahSu = "Bukti rahmat sareng kaadilan syariat Islam yén siksa dosa ngan ukur tumiba ka jalma anu ngahaja migawé kalayan eling sarta henteu aya paksaan.",
            syarahEn = "A foundational principle of compassionate Islamic jurisprudence, removing legal guilt and moral liability from involuntary slips, genuine oversights, and duress."
        ),
        40 to HadithLangData(
            titleSu = "Hirup di Dunya Siga Musafir",
            titleEn = "Living in This World as a Stranger or Traveler",
            translationSu = "Rasulullah SAW nyepeng punduk kuring sarta ngadawuh: 'Kudu aya anjeun di dunya ieu saolah-olah jalma asing atawa jalma anu keur ngambah jalan (musafir).' Ibnu Umar biasa nyarios: 'Lamun anjeun aya dina waktu soré, ulah nungguan waktu subuh; lamun aya dina waktu subuh, ulah nungguan waktu soré; mangpaatkeun waktu séhat anjeun saméméh gering, sarta mangpaatkeun waktu hirup anjeun saméméh maot.'",
            translationEn = "The Messenger of Allah took me by the shoulder and said: 'Be in this world as though you were a stranger or a wayfarer.' And Ibn Umar used to say: 'In the evening do not expect to see the morning, and in the morning do not expect to see the evening. Take from your health for your sickness, and from your life for your death.'",
            syarahSu = "Pituduh zuhud sareng kasadaran kana pondokna umur dunya, sangkan manusa getol ngumpulkeun bekel amal shalih kanggo kahirupan abadi di ahérat.",
            syarahEn = "A poignant meditation on transience, urging the spiritual traveler to view mortal life as a brief waystation and prioritize eternal preparation."
        ),
        41 to HadithLangData(
            titleSu = "Nalukkeun Hawa Nafsu kana Syariat",
            titleEn = "Subordinating Desires to Prophetic Guidance",
            translationSu = "Henteu sampurna iman salasaurang di antara anjeun sadaya nepi ka hawa nafsuna tumut kana naon-naon anu dibantun ku kuring.",
            translationEn = "None of you truly believes until his desires are subservient to that which I have brought.",
            syarahSu = "Puncak kaislaman nyaéta nalika kacintaan kalbu sareng hawa nafsu parantos tunduk sagemblengna kana hukum-hukum Allah sareng Sunnah Rasulullah SAW.",
            syarahEn = "The true summit of spiritual submission: aligning personal desires, cultural predilections, and internal inclinations completely with Divine revelation."
        ),
        42 to HadithLangData(
            titleSu = "Lega Pisan Panghampura Allah (Hadis Qudsi)",
            titleEn = "The Boundless Scope of Divine Forgiveness (Hadith Qudsi)",
            translationSu = "Wahai anak Adam, salila anjeun ngadua ka Kami sarta miharep ka Kami, Kami baris ngahampura dosa anjeun anu geus kaliwat sarta Kami henteu paduli. Wahai anak Adam, sanajan dosa anjeun numpuk nepi ka méga di langit, tuluy anjeun ménta hampura ka Kami, tangtu Kami baris ngahampura anjeun. Wahai anak Adam, lamun anjeun datang ka Kami kalayan mawa kasalahan sagedé jagat bumi, tuluy anjeun papanggih jeung Kami bari teu miduakeun Kami ku naon baé ogé, tangtu Kami baris maparin panghampura sagedé jagat bumi éta ogé.",
            translationEn = "O son of Adam, so long as you call upon Me and hope in Me, I shall forgive you for what you have done and I shall not mind. O son of Adam, were your sins to reach the clouds of the sky and then you asked for My forgiveness, I would forgive you. O son of Adam, were you to come to Me with sins nearly as great as the earth and then meet Me without having associated any partner with Me, I would bring you forgiveness nearly as great as it.",
            syarahSu = "Hadis pamungkas Arbain Nawawi anu masihan harepan sareng katenangan jiwa anu kacida ageungna: ampunan Allah langkung lega batan sagala rupi dosa manusa salami aya tobat sareng tauhid anu murni.",
            syarahEn = "The sublime conclusion of the Forty Hadith, delivering boundless solace and hope: Divine clemency and pardon will forever surpass human transgression so long as sincere repentance and unadulterated monotheism are preserved."
        )
    )
}
