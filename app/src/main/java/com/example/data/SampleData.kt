package com.example.data

import com.example.model.CartItem
import com.example.model.ChatMessage
import com.example.model.ChatSender
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.OrderTrackingStep
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.PushNotification
import com.example.model.Review

object SampleData {

    val sampleProducts: List<Product> = listOf(
        Product(
            id = "prod-1",
            name = "Organic Hass Avocados (Pack of 4)",
            brand = "SunHarvest Organics",
            category = ProductCategory.PRODUCE,
            price = 5.99,
            originalPrice = 7.49,
            rating = 4.9,
            reviewsCount = 142,
            ecoScore = "A+",
            carbonOffsetKg = 2.4,
            description = "Locally grown, hand-picked Fair Trade certified Hass avocados. Rich, buttery texture packed with healthy omega fatty acids and delivered in 100% biodegradable fiber netting.",
            highlights = listOf("Zero Pesticides", "Fair Trade Certified", "Compostable Netting", "Direct from Regenerative Farm"),
            isOrganicCertified = true,
            isFairTrade = true,
            isBestseller = true,
            isDeal = true,
            placeholderColor = 0xFF2E7D32
        ),
        Product(
            id = "prod-2",
            name = "Bamboo Utensil Travel Set",
            brand = "EcoNomad",
            category = ProductCategory.HOME,
            price = 14.50,
            originalPrice = 18.00,
            rating = 4.8,
            reviewsCount = 98,
            ecoScore = "A+",
            carbonOffsetKg = 4.1,
            description = "Handmade organically sourced Moso bamboo cutlery set including fork, spoon, serrated knife, chopsticks, and coconut fiber cleaning straw inside an organic hemp pouch.",
            highlights = listOf("100% Biodegradable", "Antimicrobial Bamboo", "Plastic-Free Packaging", "Dishwasher Safe"),
            isOrganicCertified = true,
            isFairTrade = true,
            isBestseller = true,
            isDeal = false,
            placeholderColor = 0xFF388E3C
        ),
        Product(
            id = "prod-3",
            name = "Cold-Pressed Extra Virgin Olive Oil 500ml",
            brand = "TerraVerde Estate",
            category = ProductCategory.PANTRY,
            price = 18.99,
            originalPrice = 22.50,
            rating = 4.9,
            reviewsCount = 210,
            ecoScore = "A+",
            carbonOffsetKg = 1.9,
            description = "Single-origin unfiltered cold-pressed extra virgin olive oil produced on solar-powered Tuscan groves. Bottled in recycled UV-protective dark amber glass.",
            highlights = listOf("Single Origin", "Solar-Powered Pressing", "Recycled Glass Bottle", "High Polyphenols"),
            isOrganicCertified = true,
            isFairTrade = true,
            isBestseller = true,
            isDeal = true,
            placeholderColor = 0xFF1B5E20
        ),
        Product(
            id = "prod-4",
            name = "Solar Fast-Charge Power Bank 15000mAh",
            brand = "VoltEco",
            category = ProductCategory.TECH,
            price = 45.00,
            originalPrice = 59.99,
            rating = 4.7,
            reviewsCount = 85,
            ecoScore = "A",
            carbonOffsetKg = 8.5,
            description = "High-efficiency monocrystalline solar power bank encased in recycled ocean plastics. Features dual USB-C fast charging ports, flashlight, and weatherproof design.",
            highlights = listOf("Recycled Ocean Bound Plastic", "High Sun Conversion (24%)", "USB-C PD 20W", "IP65 Weatherproof"),
            isOrganicCertified = false,
            isFairTrade = true,
            isBestseller = false,
            isDeal = true,
            placeholderColor = 0xFF00796B
        ),
        Product(
            id = "prod-5",
            name = "Recycled Organic Cotton Minimalist Tee",
            brand = "Verdant Wear",
            category = ProductCategory.FASHION,
            price = 28.00,
            originalPrice = 35.00,
            rating = 4.8,
            reviewsCount = 120,
            ecoScore = "A+",
            carbonOffsetKg = 3.6,
            description = "Breathable unisex crewneck tailored from 60% upcycled organic cotton and 40% recycled post-consumer threads. Dyed with closed-loop botanical non-toxic dyes.",
            highlights = listOf("Closed Loop Water Dyeing", "GOTS Certified Organic", "Fair Wage Certified", "Pre-shrunk Soft Touch"),
            isOrganicCertified = true,
            isFairTrade = true,
            isBestseller = false,
            isDeal = false,
            placeholderColor = 0xFF43A047
        ),
        Product(
            id = "prod-6",
            name = "Wildflower Raw Mountain Honey 350g",
            brand = "BeeKeep Sanctuary",
            category = ProductCategory.PANTRY,
            price = 12.99,
            originalPrice = 15.00,
            rating = 4.9,
            reviewsCount = 175,
            ecoScore = "A+",
            carbonOffsetKg = 1.2,
            description = "Unpasteurized raw honey harvested ethically from wild bee sanctuaries with natural flora biodiversity. 5% of all proceeds directly fund wild pollinator conservation.",
            highlights = listOf("Bee-Friendly Ethical Harvest", "Raw & Unheated", "100% Recyclable Tin Lid", "Pollinator Fund Contributor"),
            isOrganicCertified = true,
            isFairTrade = true,
            isBestseller = true,
            isDeal = false,
            placeholderColor = 0xFFF57F17
        ),
        Product(
            id = "prod-7",
            name = "Zero-Waste Solid Shampoo & Conditioner Bar",
            brand = "PureBotanica",
            category = ProductCategory.BEAUTY,
            price = 11.50,
            originalPrice = 14.00,
            rating = 4.7,
            reviewsCount = 64,
            ecoScore = "A+",
            carbonOffsetKg = 2.8,
            description = "Concentrated sulfate-free formula equals 3 liquid plastic bottles. Enriched with rosemary essential oil, shea butter, and oat protein for radiant shine.",
            highlights = listOf("Zero Plastic Waste", "Sulfate & Paraben Free", "Cruelty-Free Leaping Bunny", "Concentrated Formula"),
            isOrganicCertified = true,
            isFairTrade = true,
            isBestseller = false,
            isDeal = true,
            placeholderColor = 0xFF00897B
        ),
        Product(
            id = "prod-8",
            name = "Stainless Steel Insulated Bento Box",
            brand = "GreenLiving",
            category = ProductCategory.HOME,
            price = 24.99,
            originalPrice = 32.00,
            rating = 4.9,
            reviewsCount = 112,
            ecoScore = "A+",
            carbonOffsetKg = 5.2,
            description = "Leak-proof triple-compartment meal box made of premium 18/8 food-grade stainless steel with airtight silicone seal. Eliminates single-use plastic containers forever.",
            highlights = listOf("Food Grade 18/8 Steel", "Airtight Silicone Gasket", "Oven & Dishwasher Safe", "Lifetime Durability"),
            isOrganicCertified = false,
            isFairTrade = true,
            isBestseller = true,
            isDeal = false,
            placeholderColor = 0xFF2E7D32
        ),
        Product(
            id = "prod-9",
            name = "Ceremonial Grade Organic Uji Matcha 50g",
            brand = "ZenLeaf Kyoto",
            category = ProductCategory.PANTRY,
            price = 22.00,
            originalPrice = 28.00,
            rating = 5.0,
            reviewsCount = 188,
            ecoScore = "A+",
            carbonOffsetKg = 1.4,
            description = "First-harvest shade-grown green tea leaves stone-ground in small batches in Uji, Kyoto. Vibrant emerald color with delicate umami sweetness and zero bitterness.",
            highlights = listOf("First Spring Harvest", "Stone Ground", "JAS Organic Certified", "Nitrogen Flushed Tin"),
            isOrganicCertified = true,
            isFairTrade = true,
            isBestseller = true,
            isDeal = true,
            placeholderColor = 0xFF33691E
        )
    )

    val sampleReviews: List<Review> = listOf(
        Review(
            id = "rev-1",
            productId = "prod-1",
            author = "Sarah Jenkins",
            rating = 5,
            title = "Incredible ripeness and taste!",
            comment = "These avocados arrived perfectly intact in the eco-fiber netting! Super creamy, delicious in salads and guacamole. Love knowing they are Fair Trade.",
            date = "2 days ago",
            verifiedPurchase = true,
            helpfulCount = 19,
            tags = listOf("Fresh Produce", "Eco Packaging", "Super Creamy")
        ),
        Review(
            id = "rev-2",
            productId = "prod-1",
            author = "David Chen",
            rating = 5,
            title = "Zero waste and super fresh",
            comment = "Usually grocery store avocados are either rock hard or bruised. These arrived at the ideal ripening stage and the packaging went straight into my home compost.",
            date = "1 week ago",
            verifiedPurchase = true,
            helpfulCount = 12,
            tags = listOf("Compostable", "High Quality")
        ),
        Review(
            id = "rev-3",
            productId = "prod-2",
            author = "Elena Rostova",
            rating = 5,
            title = "My everyday travel companion",
            comment = "I carry this set everywhere in my backpack. Extremely sturdy bamboo, cleans easily with warm water, and looks gorgeous. Already bought two more as gifts!",
            date = "3 days ago",
            verifiedPurchase = true,
            helpfulCount = 8,
            tags = listOf("Durable", "Zero Waste", "Great Gift")
        ),
        Review(
            id = "rev-4",
            productId = "prod-3",
            author = "Marco Bellini",
            rating = 5,
            title = "Authentic Tuscan olive oil flavor",
            comment = "The peppery finish and fruity aroma prove this is genuine cold pressed extra virgin oil. The UV glass protection is top notch.",
            date = "4 days ago",
            verifiedPurchase = true,
            helpfulCount = 27,
            tags = listOf("Authentic Taste", "High Polyphenols")
        )
    )

    val initialSampleOrder = Order(
        id = "GC-8492",
        orderNumber = "#GC-8492",
        date = "Today at 10:15 AM",
        items = listOf(
            CartItem(sampleProducts[0], 2),
            CartItem(sampleProducts[1], 1),
            CartItem(sampleProducts[5], 1)
        ),
        subtotal = 39.47,
        discount = 4.00,
        pointsRedeemed = 100,
        pointsRedeemedDiscount = 1.00,
        shippingFee = 0.0,
        total = 34.47,
        pointsEarned = 345,
        status = OrderStatus.IN_TRANSIT,
        deliverySpeed = "Green Express (EV Delivery)",
        courierName = "Alex Rivera",
        courierVehicle = "Eco Electric Cargo Van #104",
        estimatedDeliveryTime = "Today, 3:30 PM",
        deliveryAddress = "742 Evergreen Terrace, Eco District, CA 90210",
        paymentMethod = "Google Pay (256-bit Secure)",
        trackingSteps = listOf(
            OrderTrackingStep(
                status = OrderStatus.CONFIRMED,
                title = "Order Confirmed & Verified",
                description = "Payment secured via Google Pay. Items assigned to Eco Fulfillment Hub.",
                time = "10:15 AM",
                isCompleted = true,
                isCurrent = false
            ),
            OrderTrackingStep(
                status = OrderStatus.PACKED,
                title = "Packed in 100% Recycled Kraft",
                description = "Paper tape & biodegradable cornstarch cushioning applied.",
                time = "11:30 AM",
                isCompleted = true,
                isCurrent = false
            ),
            OrderTrackingStep(
                status = OrderStatus.IN_TRANSIT,
                title = "In Transit with Clean Fleet",
                description = "Loaded onto 100% Electric Delivery Cargo Van #104. On the road.",
                time = "1:05 PM",
                isCompleted = true,
                isCurrent = true
            ),
            OrderTrackingStep(
                status = OrderStatus.OUT_FOR_DELIVERY,
                title = "Courier Near Your Neighborhood",
                description = "Alex Rivera is 8 stops away from your doorstep.",
                time = "Est. 3:00 PM",
                isCompleted = false,
                isCurrent = false
            ),
            OrderTrackingStep(
                status = OrderStatus.DELIVERED,
                title = "Delivered Safely to Porch",
                description = "Contactless delivery with photo verification.",
                time = "Est. 3:30 PM",
                isCompleted = false,
                isCurrent = false
            )
        )
    )

    val initialNotifications: List<PushNotification> = listOf(
        PushNotification(
            id = "notif-1",
            title = "Order #GC-8492 In Transit! 🚚",
            message = "Your Green Express electric van is on its way. Estimated arrival: 3:30 PM.",
            timeAgo = "25m ago",
            isRead = false,
            orderId = "GC-8492"
        ),
        PushNotification(
            id = "notif-2",
            title = "You Earned 345 GreenPoints! 🌱",
            message = "Your sustainable shopping saved 7.7 kg of CO₂. Tier status: Sprout.",
            timeAgo = "2h ago",
            isRead = false,
            orderId = null
        ),
        PushNotification(
            id = "notif-3",
            title = "Flash Deal: 20% Off Cold-Pressed Oils 🫒",
            message = "Single origin artisan oils harvested with renewable energy.",
            timeAgo = "1d ago",
            isRead = true,
            orderId = null
        )
    )

    val initialChatMessages: List<ChatMessage> = listOf(
        ChatMessage(
            id = "msg-1",
            sender = ChatSender.BOT,
            text = "Hello Maya! Welcome to GreenCart 24/7 Live Support. How can our eco-support team help you today?",
            timestamp = "10:30 AM",
            quickOptions = listOf("Track my Order #GC-8492", "Zero-Waste Packaging Details", "Redeem GreenPoints", "Speak with Live Agent")
        )
    )
}
