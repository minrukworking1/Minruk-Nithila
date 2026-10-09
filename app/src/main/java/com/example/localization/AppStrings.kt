package com.example.localization

import com.example.model.AppLanguage

object AppStrings {
    fun get(key: String, lang: AppLanguage): String {
        return translations[key]?.get(lang) ?: translations[key]?.get(AppLanguage.ENGLISH) ?: key
    }

    private val translations = mapOf(
        "app_name" to mapOf(
            AppLanguage.ENGLISH to "GreenCart",
            AppLanguage.SPANISH to "GreenCart",
            AppLanguage.FRENCH to "GreenCart",
            AppLanguage.GERMAN to "GreenCart",
            AppLanguage.ARABIC to "جرين كارت",
            AppLanguage.JAPANESE to "グリーンカート"
        ),
        "tagline" to mapOf(
            AppLanguage.ENGLISH to "Sustainable Shopping, Conscious Living",
            AppLanguage.SPANISH to "Compras sostenibles, vida consciente",
            AppLanguage.FRENCH to "Achats durables, vie consciente",
            AppLanguage.GERMAN to "Nachhaltiges Einkaufen, bewusstes Leben",
            AppLanguage.ARABIC to "تسوق مستدام، حياة واعية",
            AppLanguage.JAPANESE to "持続可能なショッピング、意識的な生活"
        ),
        "search_placeholder" to mapOf(
            AppLanguage.ENGLISH to "Search organic produce, zero-waste goods...",
            AppLanguage.SPANISH to "Buscar productos orgánicos, residuo cero...",
            AppLanguage.FRENCH to "Rechercher des produits bio, zéro déchet...",
            AppLanguage.GERMAN to "Bio-Produkte, Zero-Waste-Waren suchen...",
            AppLanguage.ARABIC to "ابحث عن المنتجات العضوية والخالية من النفايات...",
            AppLanguage.JAPANESE to "オーガニック製品やゼロウェイスト商品を検索..."
        ),
        "categories" to mapOf(
            AppLanguage.ENGLISH to "Categories",
            AppLanguage.SPANISH to "Categorías",
            AppLanguage.FRENCH to "Catégories",
            AppLanguage.GERMAN to "Kategorien",
            AppLanguage.ARABIC to "الفئات",
            AppLanguage.JAPANESE to "カテゴリー"
        ),
        "recommended_for_you" to mapOf(
            AppLanguage.ENGLISH to "Recommended For You",
            AppLanguage.SPANISH to "Recomendado para ti",
            AppLanguage.FRENCH to "Recommandé pour vous",
            AppLanguage.GERMAN to "Für Sie empfohlen",
            AppLanguage.ARABIC to "موصى به لك",
            AppLanguage.JAPANESE to "あなたへのおすすめ"
        ),
        "flash_deals" to mapOf(
            AppLanguage.ENGLISH to "Flash Deals & Eco Discounts",
            AppLanguage.SPANISH to "Ofertas relámpago y descuentos",
            AppLanguage.FRENCH to "Ventes flash et réductions",
            AppLanguage.GERMAN to "Blitzangebote & Öko-Rabatte",
            AppLanguage.ARABIC to "عروض سريعة وخصومات بيئية",
            AppLanguage.JAPANESE to "タイムセール＆エコ割引"
        ),
        "add_to_cart" to mapOf(
            AppLanguage.ENGLISH to "Add to Cart",
            AppLanguage.SPANISH to "Añadir a la cesta",
            AppLanguage.FRENCH to "Ajouter au panier",
            AppLanguage.GERMAN to "In den Warenkorb",
            AppLanguage.ARABIC to "أضف إلى السلة",
            AppLanguage.JAPANESE to "カートに追加"
        ),
        "cart" to mapOf(
            AppLanguage.ENGLISH to "Shopping Cart",
            AppLanguage.SPANISH to "Cesta de compras",
            AppLanguage.FRENCH to "Panier d'achat",
            AppLanguage.GERMAN to "Warenkorb",
            AppLanguage.ARABIC to "عربة التسوق",
            AppLanguage.JAPANESE to "ショッピングカート"
        ),
        "wishlist" to mapOf(
            AppLanguage.ENGLISH to "Wishlist",
            AppLanguage.SPANISH to "Lista de deseos",
            AppLanguage.FRENCH to "Liste de souhaits",
            AppLanguage.GERMAN to "Wunschliste",
            AppLanguage.ARABIC to "قائمة الرغبات",
            AppLanguage.JAPANESE to "お気に入り"
        ),
        "checkout" to mapOf(
            AppLanguage.ENGLISH to "Secure Checkout",
            AppLanguage.SPANISH to "Pago seguro",
            AppLanguage.FRENCH to "Paiement sécurisé",
            AppLanguage.GERMAN to "Sicherer Bezahlvorgang",
            AppLanguage.ARABIC to "الدفع الآمن",
            AppLanguage.JAPANESE to "安全なチェックアウト"
        ),
        "order_tracking" to mapOf(
            AppLanguage.ENGLISH to "Live Order Tracking",
            AppLanguage.SPANISH to "Seguimiento en vivo",
            AppLanguage.FRENCH to "Suivi en direct",
            AppLanguage.GERMAN to "Live-Sendungsverfolgung",
            AppLanguage.ARABIC to "تتبع مباشر للطلب",
            AppLanguage.JAPANESE to "リアルタイム注文追跡"
        ),
        "loyalty_rewards" to mapOf(
            AppLanguage.ENGLISH to "GreenPoints Rewards",
            AppLanguage.SPANISH to "Recompensas GreenPoints",
            AppLanguage.FRENCH to "Récompenses GreenPoints",
            AppLanguage.GERMAN to "GreenPoints Prämien",
            AppLanguage.ARABIC to "مكافآت جرين بوينتس",
            AppLanguage.JAPANESE to "グリーンポイント特典"
        ),
        "customer_support" to mapOf(
            AppLanguage.ENGLISH to "Live Support & Chat",
            AppLanguage.SPANISH to "Soporte en vivo y Chat",
            AppLanguage.FRENCH to "Support en direct et Chat",
            AppLanguage.GERMAN to "Live-Support & Chat",
            AppLanguage.ARABIC to "الدعم المباشر والدردشة",
            AppLanguage.JAPANESE to "ライブサポート＆チャット"
        ),
        "customer_reviews" to mapOf(
            AppLanguage.ENGLISH to "Customer Reviews",
            AppLanguage.SPANISH to "Reseñas de clientes",
            AppLanguage.FRENCH to "Avis des clients",
            AppLanguage.GERMAN to "Kundenbewertungen",
            AppLanguage.ARABIC to "تقييمات العملاء",
            AppLanguage.JAPANESE to "カスタマーレビュー"
        ),
        "write_review" to mapOf(
            AppLanguage.ENGLISH to "Write a Review",
            AppLanguage.SPANISH to "Escribir reseña",
            AppLanguage.FRENCH to "Écrire un avis",
            AppLanguage.GERMAN to "Bewertung schreiben",
            AppLanguage.ARABIC to "اكتب تقييماً",
            AppLanguage.JAPANESE to "レビューを書く"
        ),
        "profile" to mapOf(
            AppLanguage.ENGLISH to "My Account",
            AppLanguage.SPANISH to "Mi cuenta",
            AppLanguage.FRENCH to "Mon compte",
            AppLanguage.GERMAN to "Mein Konto",
            AppLanguage.ARABIC to "حسابي",
            AppLanguage.JAPANESE to "マイアカウント"
        ),
        "social_login_title" to mapOf(
            AppLanguage.ENGLISH to "Quick & Secure Access",
            AppLanguage.SPANISH to "Acceso rápido y seguro",
            AppLanguage.FRENCH to "Accès rapide et sécurisé",
            AppLanguage.GERMAN to "Schneller und sicherer Zugriff",
            AppLanguage.ARABIC to "وصول سريع وآمن",
            AppLanguage.JAPANESE to "すばやく安全なログイン"
        ),
        "co2_saved" to mapOf(
            AppLanguage.ENGLISH to "kg CO₂ Offset",
            AppLanguage.SPANISH to "kg CO₂ compensado",
            AppLanguage.FRENCH to "kg CO₂ compensé",
            AppLanguage.GERMAN to "kg CO₂ eingespart",
            AppLanguage.ARABIC to "كجم من انبعاثات الكربون التي تم تعويضها",
            AppLanguage.JAPANESE to "kg CO₂削減"
        ),
        "language_select" to mapOf(
            AppLanguage.ENGLISH to "Language",
            AppLanguage.SPANISH to "Idioma",
            AppLanguage.FRENCH to "Langue",
            AppLanguage.GERMAN to "Sprache",
            AppLanguage.ARABIC to "اللغة",
            AppLanguage.JAPANESE to "言語"
        )
    )
}
