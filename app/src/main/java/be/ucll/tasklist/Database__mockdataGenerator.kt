package be.ucll.tasklist

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

class Database__mockdataGenerator {

    fun generateMockCheckingsAccounts(): MutableList<Database__Account> {
        val accountsMockData = mutableListOf<Database__Account>()

        val account1 = Database__Account(
            userID = 1,
            accountID = 1,
            accountName = "ING",
            accountNumber = "BE12345678",
            totalBalance = "1000.00",
            accountType = "CheckingsAccount"
        )
        accountsMockData.add(account1)

        val account2 = Database__Account(
            userID = 1,
            accountID = 2,
            accountName = "KBC",
            accountNumber = "BE87654321",
            totalBalance = "1500.00",
            accountType = "CheckingsAccount"
        )
        accountsMockData.add(account2)

        return accountsMockData
    }

    fun generateMockSavingsAccounts(): MutableList<Database__Account> {
        val accountsMockData = mutableListOf<Database__Account>()

        val account1 = Database__Account(
            userID = 1,
            accountID = 3,
            accountName = "ING",
            accountNumber = "BE12345678",
            totalBalance = "100000.00",
            accountType = "SavingsAccount"
        )
        accountsMockData.add(account1)

        val account2 = Database__Account(
            userID = 1,
            accountID = 4,
            accountName = "KBC",
            accountNumber = "BE87654321",
            totalBalance = "150000.00",
            accountType = "SavingsAccount"
        )
        accountsMockData.add(account2)

        return accountsMockData
    }

    fun generateMockAssetAccount(): MutableList<Database__Account> {
        val accountsMockData = mutableListOf<Database__Account>()

        val account1 = Database__Account(
            userID = 1,
            accountID = 7,
            accountName = "Degiro",
            accountNumber = "BE12345678",
            totalBalance = "100000.00",
            accountType = "AssetAccount"
        )
        accountsMockData.add(account1)
        return accountsMockData
    }

    fun generateMockExtraLegalAccounts(): MutableList<Database__Account> {
        val accountsMockData = mutableListOf<Database__Account>()

        val account1 = Database__Account(
            userID = 1,
            accountID = 5,
            accountName = "Edenred",
            accountNumber = "12345678",
            totalBalance = "190.00",
            accountType = "ExtraLegalAccount"
        )
        accountsMockData.add(account1)

        val account2 = Database__Account(
            userID = 1,
            accountID = 6,
            accountName = "Monizze",
            accountNumber = "87654321",
            totalBalance = "250.00",
            accountType = "ExtraLegalAccount"
        )
        accountsMockData.add(account2)

        return accountsMockData
    }

    fun generateAssetMockDataAssetAccounts(): MutableList<Database__Asset> {
        val mockDataList = mutableListOf<Database__Asset>()

        val stockTickers = listOf("AAPL", "GOOGL", "MSFT", "AMZN", "TSLA", "FB", "V", "NVDA", "PYPL", "NFLX")
        val stockFullNames = listOf("Apple", "Google", "Microsoft", "Amazon", "Tesla", "Meta", "Visa", "NVIDIA", "PayPal", "Netflix")
        mockDataList.addAll(generateMockDataForType("Stocks", 9, stockTickers, stockFullNames))

        val etfTickers = listOf("SPY", "QQQ", "VTI", "IWM", "EFA", "EEM", "GLD", "SLV", "VWO", "VNQ")
        val etfFullNames = listOf("S&P500", "Invesco", "Allworld", "Russell", "MSCI", "Emerging Markets", "Gold Trust", "Silver Trust", "Emerging Markets", "Real Estate")
        mockDataList.addAll(generateMockDataForType("ETFs", 9, etfTickers, etfFullNames))

        val cryptoTickers = listOf("BTC", "ETH", "XRP", "LTC", "BCH", "ADA", "DOT", "LINK", "XLM", "DOGE")
        val cryptoFullNames = listOf("Bitcoin", "Ethereum", "Ripple", "Litecoin", "Bitcoin Cash", "Cardano", "Polkadot", "Chainlink", "Stellar", "Dogecoin")
        mockDataList.addAll(generateMockDataForType("Cryptos", 9, cryptoTickers, cryptoFullNames))

        val obligationTickers = listOf("GOVT", "AGG", "BND", "LQD", "HYG", "TLT", "IEF", "MUB", "MINT", "BOND")
        val obligationFullNames = listOf("U.S. Treasury Bond", "Aggregate Bond", "Bond Market", "Corporate Bond", "High Yield ETF", "20+ Year Treasury Bond", "7-10 Year Treasury Bond", "National Muni Bond", "Maturity Active", "Total Return")
        mockDataList.addAll(generateMockDataForType("Obligations", 9, obligationTickers, obligationFullNames))

        return mockDataList
    }

    private fun generateMockDataForType(type: String, count: Int, tickers: List<String>, fullname: List<String>): List<Database__Asset> {
        val dataList = mutableListOf<Database__Asset>()
        val random = Random()

        for (i in 1..count) {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            val investment = Database__Asset(
                userID = 1,
                investmentId = random.nextLong(),
                accountID = 7,
                investmentType = type,
                name = fullname[i],
                ticker = tickers[i],
                quantity = (random.nextDouble() * (10.0 - 1.0) + 1.0).toInt(),
                lastValue = random.nextDouble() * (1000.0 - 1.0) + 1.0,
                lastUpdated = dateFormat.parse("2/12/2023").toString(),
            )
            dataList.add(investment)
        }

        return dataList
    }

    fun generatePriceEvolutionData(investments: List<Database__Asset>): List<Database__PriceAsset> {
        val mockData = mutableListOf<Database__PriceAsset>()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        val random = Random()

        for (investment in investments) {
            calendar.time = Date() // Set the calendar to the current date

            for (i in 1..30) {
                val dateOfPrice = dateFormat.format(calendar.time)
                val openPrice = String.format("%.2f", (random.nextDouble() * (200.0 - 50.0) + 50.0))
                val closePrice = String.format("%.2f", (random.nextDouble() * (200.0 - 50.0) + 50.0))
                val highPrice = String.format("%.2f", (random.nextDouble() * (200.0 - 50.0) + 50.0))
                val lowPrice = String.format("%.2f", (random.nextDouble() * (200.0 - 50.0) + 50.0))
                val volume = String.format("%.2f", (random.nextDouble() * (5000.0 - 1000.0) + 1000.0))

                val priceAsset = Database__PriceAsset(
                    investmentId = investment.investmentId,
                    accountID = investment.accountID,
                    dateOfPrice = dateOfPrice,
                    openPrice = openPrice,
                    closePrice = closePrice,
                    highPrice = highPrice,
                    lowPrice = lowPrice,
                    volume = volume
                )

                mockData.add(priceAsset)
                calendar.add(Calendar.DAY_OF_MONTH, -1)
            }
        }

        return mockData
    }

    fun generateMockTransactionData(investments: List<Database__Asset>): List<Database__AssetTransaction> {
        val mockData = mutableListOf<Database__AssetTransaction>()
        val random = Random()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        for(investment in investments) {
            for (i in 1..3) {
                val transactionType = if (random.nextBoolean()) "Buy" else "Sell"
                val quantity = random.nextInt(15) + 1
                val dateBuy = dateFormat.format(Date())

                val calendar = Calendar.getInstance()
                calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateBuy)
                calendar.add(Calendar.DAY_OF_MONTH, random.nextInt(30) + 1) // Add 1 to 30 days
                dateFormat.format(calendar.time)

                val dateSell = dateFormat.format(calendar.time)

                val valueBuy = random.nextDouble() * 999 + 1

                val mockTransaction = Database__AssetTransaction(
                    investmentId = investment.investmentId,
                    transactionType = transactionType,
                    quantity = quantity,
                    transactionDate = dateSell,
                    value = valueBuy,
                    accountID = 7,
                    userID = 1
                )

                mockData.add(mockTransaction)
            }
        }

        return mockData
    }

    fun generateTransactionsMockDataCheckingsAccounts(): MutableList<Database__Transaction> {
        val transactions = mutableListOf<Database__Transaction>()

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val random = Random()

        val companies = listOf(
            "Acme Electronics", "Bright Foods", "Quantum Motors", "SolarTech Solutions", "GreenWave Energy",
            "AquaHealth Labs", "Swift Couriers", "UrbanStyle Apparel", "EcoGrocery Mart", "TechVibe Innovations",
            "FusionFitness Studios", "FreshFlare Florists", "QuickBite Eateries", "Stellar Stationery", "Zenith Pharmaceuticals",
            "SunSip Beverages", "BlazeGear Sports", "UrbanCraft Furniture", "ByteTech Computers", "PurePlates Kitchen",
            "NatureNook Nursery", "GigaGrocery", "MetroMingle Events", "PetalPush Floral", "Skyward Airlines",
            "EcoFuel Energy", "WellFit Pharmacy", "UrbanBlend Coffee", "SparkPlug Auto Parts", "BioBites Organics",
            "SummitStyles Outfitters", "CosmicConnect Telecom", "FreshFeast Catering", "BioTech Health", "SolarFlame Appliances",
            "GearUp Gadgets", "QuickFix Repairs", "AquaTreat Water", "SwiftShine Cleaners", "VivaVideo Productions",
            "ZenZone Yoga", "GreenWheel Transport", "PixelPlay Games", "PurePrint Media", "AquaBrew Coffee",
            "QuantumQuill Stationery", "UrbanHarbor Cruises", "FreshFuel Grocers", "SparkSprint Telecom", "BioBloom Florists",
            "SkySavor Airlines", "UrbanSculpt Furniture", "SunCraft Solar", "ByteBite Tech", "QuantumQuench Beverages",
            "BlazeFit Athletics", "EcoElite Fashion", "AquaGro Gardens", "SwiftServe Catering", "BioBreeze Health",
            "StellarSound Electronics", "SolarSip Drinks", "UrbanRide Cabs", "GigaGear Gadgets", "FreshFurnish Home",
            "GreenGen Energy", "ByteBrew Coffee", "BlazeTech Auto Parts", "AquaCraft Plumbing", "QuantumQuick Repairs",
            "UrbanGrocery Mart", "FreshFlair Fashion", "Skyward Solar", "TechTide Innovations", "GreenFuel Energy",
            "SwiftSip Beverages", "BioBlast Health", "BlazeBite Sports", "AquaStyle Apparel", "QuantumQuill Books", "Acme Electronics"
        )

        val desciptions = listOf(
            "Smartphones", "Fresh produce", "Car accessories", "Solar panels", "Renewable energy plans",
            "Bottled water", "Courier services", "Clothing", "Organic groceries", "Laptops",
            "Gym memberships", "Bouquets", "Fast food", "Office supplies", "Medications",
            "Soft drinks", "Sportswear", "Furniture", "Computer accessories", "Dinnerware",
            "Plants", "Bulk groceries", "Event planning services", "Floral arrangements", "Airline tickets",
            "Biofuel", "Health supplements", "Coffee beans", "Auto parts", "Organic snacks",
            "Clothing", "Phone plans", "Catering services", "Biotech products", "Home appliances",
            "Gadgets", "Repair services", "Water filters", "Cleaning services", "Video production services",
            "Yoga classes", "Transportation services", "Video games", "Printing services", "Coffee",
            "Stationery", "Cruise tickets", "Fuel for vehicles", "Telecommunications", "Flower seeds",
            "Airline services", "Sculptures", "Solar-powered gadgets", "Tech gadgets", "Beverages",
            "Athletic gear", "Eco-friendly clothing", "Garden supplies", "Catering services", "Health products",
            "Audio equipment", "Solar-powered drinks cooler", "Ride-hailing services", "Gadgets", "Home furnishings",
            "Green energy plans", "Coffee beans", "Auto parts", "Plumbing services", "Quick repair services",
            "Urban groceries", "Fashion items", "Solar panels", "Innovative tech products", "Green fuel",
            "Beverages", "Health supplements", "Sports equipment", "Water-resistant clothing", "Books", "Smartphones"
        )

        for (i in 1..80) {
            val transactionDate = dateFormatter.format(Date(System.currentTimeMillis() - (i * 86400000L))) // Generate dates for the past 80 days
            val isDeposit = random.nextBoolean()
            val type = if (isDeposit) "Deposit" else "Expense"

            val transaction = Database__Transaction(
                userID = 1,
                accountID = 1,
                companyName = companies[i],
                description = desciptions[i],
                transactionDate = transactionDate,
                category = "Category $i",
                amount = Math.round(1.0 + random.nextDouble() * 99.0).toDouble(),
                type = type
            )

            transactions.add(transaction)
        }

        for (i in 1..80) {
            val transactionDate = dateFormatter.format(Date(System.currentTimeMillis() - (i * 86400000L))) // Generate dates for the past 80 days
            val isDeposit = random.nextBoolean()
            val type = if (isDeposit) "Deposit" else "Expense"

            val transaction = Database__Transaction(
                userID = 1,
                accountID = 2,
                companyName = companies[i],
                description = desciptions[i],
                transactionDate = transactionDate,
                category = "Category $i",
                amount = Math.round(1.0 + random.nextDouble() * 99.0).toDouble(),
                type = type
            )

            transactions.add(transaction)
        }

        return transactions
    }

    fun generateTransactionsMockDataSavingsAccounts(): MutableList<Database__Transaction> {
        val transactions = mutableListOf<Database__Transaction>()

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val random = Random()

        val companies = listOf(
            "Acme Electronics", "Bright Foods", "Quantum Motors", "SolarTech Solutions", "GreenWave Energy",
            "AquaHealth Labs", "Swift Couriers", "UrbanStyle Apparel", "EcoGrocery Mart", "TechVibe Innovations",
            "FusionFitness Studios", "FreshFlare Florists", "QuickBite Eateries", "Stellar Stationery", "Zenith Pharmaceuticals",
            "SunSip Beverages", "BlazeGear Sports", "UrbanCraft Furniture", "ByteTech Computers", "PurePlates Kitchen",
            "NatureNook Nursery", "GigaGrocery", "MetroMingle Events", "PetalPush Floral", "Skyward Airlines",
            "EcoFuel Energy", "WellFit Pharmacy", "UrbanBlend Coffee", "SparkPlug Auto Parts", "BioBites Organics",
            "SummitStyles Outfitters", "CosmicConnect Telecom", "FreshFeast Catering", "BioTech Health", "SolarFlame Appliances",
            "GearUp Gadgets", "QuickFix Repairs", "AquaTreat Water", "SwiftShine Cleaners", "VivaVideo Productions",
            "ZenZone Yoga", "GreenWheel Transport", "PixelPlay Games", "PurePrint Media", "AquaBrew Coffee",
            "QuantumQuill Stationery", "UrbanHarbor Cruises", "FreshFuel Grocers", "SparkSprint Telecom", "BioBloom Florists",
            "SkySavor Airlines", "UrbanSculpt Furniture", "SunCraft Solar", "ByteBite Tech", "QuantumQuench Beverages",
            "BlazeFit Athletics", "EcoElite Fashion", "AquaGro Gardens", "SwiftServe Catering", "BioBreeze Health",
            "StellarSound Electronics", "SolarSip Drinks", "UrbanRide Cabs", "GigaGear Gadgets", "FreshFurnish Home",
            "GreenGen Energy", "ByteBrew Coffee", "BlazeTech Auto Parts", "AquaCraft Plumbing", "QuantumQuick Repairs",
            "UrbanGrocery Mart", "FreshFlair Fashion", "Skyward Solar", "TechTide Innovations", "GreenFuel Energy",
            "SwiftSip Beverages", "BioBlast Health", "BlazeBite Sports", "AquaStyle Apparel", "QuantumQuill Books", "Acme Electronics"
        )

        val desciptions = listOf(
            "Smartphones", "Fresh produce", "Car accessories", "Solar panels", "Renewable energy plans",
            "Bottled water", "Courier services", "Clothing", "Organic groceries", "Laptops",
            "Gym memberships", "Bouquets", "Fast food", "Office supplies", "Medications",
            "Soft drinks", "Sportswear", "Furniture", "Computer accessories", "Dinnerware",
            "Plants", "Bulk groceries", "Event planning services", "Floral arrangements", "Airline tickets",
            "Biofuel", "Health supplements", "Coffee beans", "Auto parts", "Organic snacks",
            "Clothing", "Phone plans", "Catering services", "Biotech products", "Home appliances",
            "Gadgets", "Repair services", "Water filters", "Cleaning services", "Video production services",
            "Yoga classes", "Transportation services", "Video games", "Printing services", "Coffee",
            "Stationery", "Cruise tickets", "Fuel for vehicles", "Telecommunications", "Flower seeds",
            "Airline services", "Sculptures", "Solar-powered gadgets", "Tech gadgets", "Beverages",
            "Athletic gear", "Eco-friendly clothing", "Garden supplies", "Catering services", "Health products",
            "Audio equipment", "Solar-powered drinks cooler", "Ride-hailing services", "Gadgets", "Home furnishings",
            "Green energy plans", "Coffee beans", "Auto parts", "Plumbing services", "Quick repair services",
            "Urban groceries", "Fashion items", "Solar panels", "Innovative tech products", "Green fuel",
            "Beverages", "Health supplements", "Sports equipment", "Water-resistant clothing", "Books", "Smartphones"
        )

        for (i in 1..80) {
            val transactionDate = dateFormatter.format(Date(System.currentTimeMillis() - (i * 86400000L))) // Generate dates for the past 80 days
            val isDeposit = random.nextBoolean()
            val type = if (isDeposit) "Deposit" else "Expense"

            val transaction = Database__Transaction(
                userID = 1,
                accountID = 3,
                companyName = companies[i],
                description = desciptions[i],
                transactionDate = transactionDate,
                category = "Category $i",
                amount = Math.round(1.0 + random.nextDouble() * 99.0).toDouble(),
                type = type
            )

            transactions.add(transaction)
        }

        for (i in 1..80) {
            val transactionDate = dateFormatter.format(Date(System.currentTimeMillis() - (i * 86400000L))) // Generate dates for the past 80 days
            val isDeposit = random.nextBoolean()
            val type = if (isDeposit) "Deposit" else "Expense"

            val transaction = Database__Transaction(
                userID = 1,
                accountID = 4,
                companyName = companies[i],
                description = desciptions[i],
                transactionDate = transactionDate,
                category = "Category $i",
                amount = Math.round(1.0 + random.nextDouble() * 99.0).toDouble(),
                type = type
            )

            transactions.add(transaction)
        }

        return transactions
    }

    fun generateTransactionsMockDataExtraLegalAccounts(): MutableList<Database__Transaction> {
        val transactions = mutableListOf<Database__Transaction>()

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val random = Random()

        val companies = listOf(
            "Acme Electronics", "Bright Foods", "Quantum Motors", "SolarTech Solutions", "GreenWave Energy",
            "AquaHealth Labs", "Swift Couriers", "UrbanStyle Apparel", "EcoGrocery Mart", "TechVibe Innovations",
            "FusionFitness Studios", "FreshFlare Florists", "QuickBite Eateries", "Stellar Stationery", "Zenith Pharmaceuticals",
            "SunSip Beverages", "BlazeGear Sports", "UrbanCraft Furniture", "ByteTech Computers", "PurePlates Kitchen",
            "NatureNook Nursery", "GigaGrocery", "MetroMingle Events", "PetalPush Floral", "Skyward Airlines",
            "EcoFuel Energy", "WellFit Pharmacy", "UrbanBlend Coffee", "SparkPlug Auto Parts", "BioBites Organics",
            "SummitStyles Outfitters", "CosmicConnect Telecom", "FreshFeast Catering", "BioTech Health", "SolarFlame Appliances",
            "GearUp Gadgets", "QuickFix Repairs", "AquaTreat Water", "SwiftShine Cleaners", "VivaVideo Productions",
            "ZenZone Yoga", "GreenWheel Transport", "PixelPlay Games", "PurePrint Media", "AquaBrew Coffee",
            "QuantumQuill Stationery", "UrbanHarbor Cruises", "FreshFuel Grocers", "SparkSprint Telecom", "BioBloom Florists",
            "SkySavor Airlines", "UrbanSculpt Furniture", "SunCraft Solar", "ByteBite Tech", "QuantumQuench Beverages",
            "BlazeFit Athletics", "EcoElite Fashion", "AquaGro Gardens", "SwiftServe Catering", "BioBreeze Health",
            "StellarSound Electronics", "SolarSip Drinks", "UrbanRide Cabs", "GigaGear Gadgets", "FreshFurnish Home",
            "GreenGen Energy", "ByteBrew Coffee", "BlazeTech Auto Parts", "AquaCraft Plumbing", "QuantumQuick Repairs",
            "UrbanGrocery Mart", "FreshFlair Fashion", "Skyward Solar", "TechTide Innovations", "GreenFuel Energy",
            "SwiftSip Beverages", "BioBlast Health", "BlazeBite Sports", "AquaStyle Apparel", "QuantumQuill Books", "Acme Electronics"
        )

        val desciptions = listOf(
            "Smartphones", "Fresh produce", "Car accessories", "Solar panels", "Renewable energy plans",
            "Bottled water", "Courier services", "Clothing", "Organic groceries", "Laptops",
            "Gym memberships", "Bouquets", "Fast food", "Office supplies", "Medications",
            "Soft drinks", "Sportswear", "Furniture", "Computer accessories", "Dinnerware",
            "Plants", "Bulk groceries", "Event planning services", "Floral arrangements", "Airline tickets",
            "Biofuel", "Health supplements", "Coffee beans", "Auto parts", "Organic snacks",
            "Clothing", "Phone plans", "Catering services", "Biotech products", "Home appliances",
            "Gadgets", "Repair services", "Water filters", "Cleaning services", "Video production services",
            "Yoga classes", "Transportation services", "Video games", "Printing services", "Coffee",
            "Stationery", "Cruise tickets", "Fuel for vehicles", "Telecommunications", "Flower seeds",
            "Airline services", "Sculptures", "Solar-powered gadgets", "Tech gadgets", "Beverages",
            "Athletic gear", "Eco-friendly clothing", "Garden supplies", "Catering services", "Health products",
            "Audio equipment", "Solar-powered drinks cooler", "Ride-hailing services", "Gadgets", "Home furnishings",
            "Green energy plans", "Coffee beans", "Auto parts", "Plumbing services", "Quick repair services",
            "Urban groceries", "Fashion items", "Solar panels", "Innovative tech products", "Green fuel",
            "Beverages", "Health supplements", "Sports equipment", "Water-resistant clothing", "Books", "Smartphones"
        )

        for (i in 1..80) {
            val transactionDate = dateFormatter.format(Date(System.currentTimeMillis() - (i * 86400000L))) // Generate dates for the past 80 days
            val isDeposit = random.nextBoolean()
            val type = if (isDeposit) "Deposit" else "Expense"

            val transaction = Database__Transaction(
                userID = 1,
                accountID = 5,
                companyName = companies[i],
                description = desciptions[i],
                transactionDate = transactionDate,
                category = "Category $i",
                amount = Math.round(1.0 + random.nextDouble() * 99.0).toDouble(),
                type = type
            )

            transactions.add(transaction)
        }

        for (i in 1..80) {
            val transactionDate = dateFormatter.format(Date(System.currentTimeMillis() - (i * 86400000L))) // Generate dates for the past 80 days
            val isDeposit = random.nextBoolean()
            val type = if (isDeposit) "Deposit" else "Expense"

            val transaction = Database__Transaction(
                userID = 1,
                accountID = 6,
                companyName = companies[i],
                description = desciptions[i],
                transactionDate = transactionDate,
                category = "Category $i",
                amount = Math.round(1.0 + random.nextDouble() * 99.0).toDouble(),
                type = type
            )

            transactions.add(transaction)
        }

        return transactions
    }
}