package com.example.buy_it.data.datasource.local

import com.example.buy_it.data.dtos.CreateProductDto

object FirestoreSeedProducts {

    val items = listOf(
        CreateProductDto(
            name = "Arroz blanco 1 kg",
            brand = "Diana",
            imageUrl = "https://jumbocolombiaio.vtexassets.com/arquivos/ids/186299/7702511000021.jpg?v=637813981775570000",
            description = "Presentación estándar para el mercado familiar.",
            range = "$4.500 - $7.500"
        ),
        CreateProductDto(
            name = "Aceite vegetal 1L",
            brand = "Premier",
            imageUrl = "https://exitocol.vtexassets.com/arquivos/ids/29609992/Aceite-Vegetal-FRESCAMPO-900-ml-3257072_a.jpg?v=638906039924370000",
            description = "Ideal para cocinar en el día a día.",
            range = "$10.000 - $16.000"
        ),
        CreateProductDto(
            name = "Pasta espagueti 500 g",
            brand = "Doria",
            imageUrl = "https://olimpica.vtexassets.com/arquivos/ids/991178/7702020113014.jpg?v=638062189313430000",
            description = "Pasta larga ideal para almuerzos familiares y preparaciones rápidas.",
            range = "$3.000 - $6.000"
        ),
        CreateProductDto(
            name = "Fríjol cargamanto 500 g",
            brand = "La Especial",
            imageUrl = "https://olimpica.vtexassets.com/arquivos/ids/2354799/7701008008236.jpg?v=639071316830630000",
            description = "Grano seco para comidas tradicionales.",
            range = "$7.000 - $12.000"
        ),
        CreateProductDto(
            name = "Lenteja 500 g",
            brand = "Diana",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7k6V1Ii3Q9m1HAnfonJDUGdQ-psocfoULQA&s",
            description = "Fuente de proteína vegetal para sopas, guisos y ensaladas.",
            range = "$5.000 - $9.000"
        ),
        CreateProductDto(
            name = "Leche entera 1L",
            brand = "Alpina",
            imageUrl = "https://supermercadolaestacion.com/58206-large_default/leche-liquida-entera-alqueria-x-1-litro.jpg",
            description = "Producto básico de refrigerador.",
            range = "$4.000 - $6.500"
        ),
        CreateProductDto(
            name = "Yogurt bebible 200 ml",
            brand = "Alpina",
            imageUrl = "https://superxtrapanama.vtexassets.com/arquivos/ids/159019/88209161357.png?v=637806398531830000",
            description = "Bebida láctea práctica para onces, lonchera o snack.",
            range = "$2.500 - $4.500"
        ),
        CreateProductDto(
            name = "Queso campesino 250 g",
            brand = "Colanta",
            imageUrl = "https://lacteoslevelma.com/wp-content/uploads/2023/06/Campesino-500-100.jpg",
            description = "Para desayuno, arepas y sándwiches.",
            range = "$10.000 - $18.000"
        ),
        CreateProductDto(
            name = "Huevos AA x 30",
            brand = "Ovoplus",
            imageUrl = "https://huevosal100.com.co/wp-content/uploads/2024/05/Sin-titulo-43.png",
            description = "Cubeta de huevos frescos, esencial para desayuno y repostería.",
            range = "$18.000 - $28.000"
        ),
        CreateProductDto(
            name = "Mantequilla 250 g",
            brand = "Alpina",
            imageUrl = "https://eurosuper.vtexassets.com/arquivos/ids/156530/7702129030250.jpg?v=638236633516300000",
            description = "Para pan y recetas caseras.",
            range = "$9.000 - $15.000"
        ),
        CreateProductDto(
            name = "Galletas de chocolate 8 und",
            brand = "Noel",
            imageUrl = "https://superlukita.com/wp-content/uploads/2024/01/GALLETAS-RELLENITAS-DIA-CHOCOLATE-x-8-UND.png",
            description = "Snack clásico para lonchera.",
            range = "$3.000 - $6.000"
        ),
        CreateProductDto(
            name = "Café molido 250 g",
            brand = "Sello Rojo",
            imageUrl = "https://carulla.vtexassets.com/arquivos/ids/21742415/CAFE-MOLIDO-523968_b.jpg?v=638876686325670000",
            description = "Producto tradicional para el hogar colombiano.",
            range = "$12.000 - $22.000"
        )
    )
}