package io.groovin.expandablebox.sampleapp.article

data class Article(
    val title: String,
    val contents: String
)


internal fun getArticleList(): List<Article> {
    return sampleArticleList
}

/** Note
 *  The articles below are created by ChatGPT, and used for UI examples without no commercial intentions.
 **/
private val sampleArticleList = listOf(
    Article(
        title = "Kotlin",
        contents = "Kotlin is a modern programming language designed to be concise, expressive, and interoperable with existing Java code.\n" +
                "It was developed by JetBrains and officially supported for Android app development by Google.\n" +
                "With Kotlin, developers can write clean and readable code, reducing boilerplate and increasing productivity.\n" +
                "It offers features like null safety, type inference, lambda expressions, and extension functions, making it a powerful language for both backend and frontend development.\n" +
                "Kotlin's seamless interoperability with Java allows for easy migration and integration with existing Java projects.\n" +
                "Its popularity has been steadily increasing, and it is widely adopted in the Android development community as an alternative to Java."
    ),
    Article(
        title = "Android Compose",
        contents = "Android Compose is a modern declarative UI toolkit for building Android applications.\n" +
                "It offers a more intuitive and efficient way to create user interfaces compared to the traditional View-based system.\n" +
                "Compose simplifies UI development by using a reactive programming model and a Kotlin-based DSL (Domain Specific Language).\n" +
                "It provides a flexible and composable architecture, allowing developers to easily reuse and combine UI components.\n" +
                "With Compose, you can build dynamic and interactive interfaces with less boilerplate code.\n" +
                "It supports real-time previewing, which helps in quickly iterating and fine-tuning the UI.\n" +
                "Compose offers a reactive approach to UI updates, automatically updating the UI when the underlying data changes.\n" +
                "It promotes a more modular and testable code structure, making it easier to write unit tests for UI components.\n" +
                "Compose is designed to work seamlessly with existing Android code, allowing for a gradual adoption and integration.\n" +
                "Overall, Android Compose empowers developers to create beautiful and responsive user interfaces with improved productivity and maintainability."
    ),
    Article(
        title = "Introducing Republic of Korea",
        contents = "South Korea, officially known as the Republic of Korea, is a vibrant and dynamic country located in East Asia.\n" +
                "With a rich history spanning thousands of years, Korea has a unique cultural heritage and traditions.\n" +
                "The capital city, Seoul, is a bustling metropolis and a global hub of technology, finance, and culture.\n" +
                "South Korea is renowned for its advanced technology industry, including electronics, automotive, and telecommunications.\n" +
                "The country is home to globally recognized brands like Samsung, LG, Hyundai, and Kia.\n" +
                "Korean cuisine, such as kimchi, bulgogi, and bibimbap, is famous worldwide for its distinctive flavors and healthy ingredients.\n" +
                "Korea has a diverse landscape, from beautiful mountain ranges like the iconic Seoraksan to stunning coastal areas like Jeju Island.\n" +
                "The Korean Wave, or 'Hallyu' has spread Korean pop music (K-pop), dramas (K-dramas), and movies to a global audience, fostering cultural exchange.\n" +
                "South Korea has a strong education system and is known for its high academic achievement and emphasis on innovation.\n" +
                "Korean people are known for their hospitality, respect for elders, and strong sense of community, making it a welcoming destination for visitors."
    ),
    Article(
        title = "What is Github?",
        contents = "GitHub is a web-based platform for version control and collaboration, primarily used for software development projects.\n" +
                "It provides a distributed version control system called Git, which allows developers to track changes in their codebase over time.\n" +
                "GitHub offers features like pull requests, issue tracking, and project management tools, making it a comprehensive platform for team collaboration.\n" +
                "Developers can use GitHub to host their repositories, share code with others, and contribute to open-source projects.\n" +
                "The platform has a robust ecosystem with integrations and extensions, enabling seamless integration with various development tools and services.\n" +
                "GitHub has a large and active community of developers, making it a hub for knowledge sharing, code review, and collaboration among software professionals."
    ),
    Article(
        title = "History of Internet",
        contents = "The history of the Internet dates back to the 1960s, when the U.S. Department of Defense initiated a research project called ARPANET to establish a decentralized network for sharing information.\n\n"+
                "ARPANET became the foundation for the development of the Internet, with the first successful message transmission occurring in 1969 between two computers at different universities.\n"+
                "In the 1970s and 1980s, the Internet expanded rapidly, connecting more universities, research institutions, and government organizations.\n"+
                "The development of TCP/IP (Transmission Control Protocol/Internet Protocol) in the 1980s standardized the communication protocols used on the Internet and facilitated its global growth.\n"+
                "The World Wide Web, created by Sir Tim Berners-Lee in 1989, revolutionized the Internet by introducing a system of interlinked hypertext documents accessible through browsers.\n"+
                "The 1990s witnessed a significant increase in public awareness and usage of the Internet, as commercial ISPs (Internet Service Providers) emerged, providing access to individuals and businesses.\n"+
                "The advent of graphical web browsers, such as Mosaic and Netscape Navigator, made the Internet more user-friendly and accessible to a broader audience.\n"+
                "E-commerce emerged as a significant aspect of the Internet in the late 1990s, with the rise of online retail and the establishment of secure payment systems.\n"+
                "The early 2000s saw the rapid expansion of social networking platforms, such as MySpace and later Facebook, which transformed online communication and social interactions.\n\n"+
                "Today, the Internet is an integral part of modern society, enabling global connectivity, instant communication, vast information resources, online entertainment, and the growth of digital economies."
    ),
    Article(
        title = "Summary of AI evolution",
        contents = "Early AI and Machine Learning: In the 1950s to 1960s, early research focused on machine learning and artificial neural networks, laying the foundation for AI.\n\n" +
                "Expert Systems and Knowledge Representation: In the 1970s and 1980s, expert systems were developed to mimic human expertise in specific domains. Knowledge representation techniques were used to store and reason with information.\n\n" +
                "Neural Networks Resurgence: In the 1990s, neural networks experienced a resurgence, driven by advancements in computing power and algorithmic improvements such as backpropagation.\n\n" +
                "Big Data and Deep Learning: In the 2000s, the availability of vast amounts of data and advances in computational capabilities fueled the growth of deep learning. Deep neural networks with many layers demonstrated remarkable performance in various tasks.\n\n" +
                "AI Applications: In recent years, AI has gained widespread adoption across industries. Natural language processing, computer vision, robotics, and recommendation systems are among the many fields benefiting from AI advancements.\n\n" +
                "Ethical Considerations: As AI becomes more prevalent, ethical considerations surrounding privacy, bias, transparency, and accountability have gained attention, leading to ongoing discussions and efforts to ensure responsible AI development and deployment.\n\n" +
                "Overall, AI has evolved from early concepts to advanced techniques that have transformed various aspects of our lives, opening up new possibilities for innovation and problem-solving."
    ),
    Article(
        title = "Introduction of Korean cuisine",
        contents = "Korean cuisine, known for its bold flavors and diverse dishes, offers a unique culinary experience. Influenced by centuries of tradition, Korean food combines fresh ingredients, vibrant colors, and a harmonious balance of flavors.\n\n" +
                "One iconic dish is kimchi, a fermented side dish made from vegetables such as cabbage or radishes, seasoned with spices. It is spicy, tangy, and plays a central role in Korean meals.\n\n" +
                "Bulgogi, thinly sliced marinated beef, is another popular dish. It is grilled to perfection, resulting in tender and flavorful meat. Often served with rice and accompanied by various side dishes, it embodies the concept of 'bap' (rice) and 'ssam' (wrap) in Korean cuisine.\n\n" +
                "Bibimbap, meaning 'mixed rice', is a colorful bowl of rice topped with an assortment of seasoned vegetables, meat, and a fried egg. It is enjoyed by mixing all the ingredients together with spicy gochujang sauce.\n\n" +
                "Korean barbecue, or 'gogi-gui' is a social dining experience. Grilled meat, such as pork belly (samgyeopsal) or beef short ribs (galbi), is enjoyed with a variety of accompaniments like lettuce wraps, dipping sauces, and pickled vegetables.\n\n" +
                "Korean street food is also popular, offering a wide range of delicious snacks. Tteokbokki, spicy rice cakes, and hotteok, sweet pancakes filled with syrup and nuts, are among the favorites.\n\n" +
                "To complete a meal, traditional desserts like bingsu, shaved ice with sweet toppings, or hotteok filled with sweet red bean paste are savored.\n\n" +
                "Korean cuisine reflects the nation's history, geography, and cultural diversity. It continues to captivate taste buds worldwide, inviting everyone to indulge in the rich and flavorful world of Korean food.\n\n"
    ),
    Article(
        title = "Global Warming",
        contents = "Global warming refers to the long-term increase in Earth's average surface temperature due to the buildup of greenhouse gases in the atmosphere.\n\n" +
                "Human activities, such as burning fossil fuels, deforestation, and industrial processes, are the primary drivers of the increased greenhouse gas emissions.\n\n" +
                "These greenhouse gases, including carbon dioxide (CO2) and methane (CH4), trap heat from the sun, leading to a warming effect known as the greenhouse effect.\n\n" +
                "The consequences of global warming include rising temperatures, melting polar ice caps and glaciers, more frequent and intense extreme weather events, and disruptions to ecosystems and biodiversity.\n\n" +
                "The impacts of global warming are wide-ranging and can affect various sectors, including agriculture, water resources, coastal areas, and human health.\n\n" +
                "Mitigating global warming requires collective efforts to reduce greenhouse gas emissions, transition to clean and renewable energy sources, promote sustainable practices, and adapt to the changes already occurring."
    ),
    Article(
        title = "Vegetarian Culture",
        contents = "Vegan culture is a lifestyle and philosophy that seeks to avoid the use of animal products for ethical, environmental, and health reasons. Vegans follow a plant-based diet, abstaining from consuming meat, poultry, seafood, dairy, eggs, and honey. They also avoid using or wearing products derived from animals, such as leather, fur, and wool.\n\n" +
                "Veganism promotes compassion towards animals and aims to reduce animal exploitation in all aspects of life. It emphasizes the belief that animals have the right to live free from harm and exploitation.\n\n" +
                "The popularity of veganism has grown significantly in recent years, leading to the emergence of a vibrant vegan community and an increase in vegan-friendly options in food, fashion, and personal care industries. Veganism is not only about dietary choices but also about making conscious and sustainable decisions that align with the values of compassion, environmental stewardship, and personal health.\n\n" +
                "Vegans often advocate for animal rights, engage in activism, and promote the benefits of plant-based living. They seek to raise awareness about the impacts of animal agriculture on animal welfare, deforestation, climate change, and public health.\n\n" +
                "The vegan movement has inspired the development of creative and delicious plant-based recipes, innovative substitutes for animal-based products, and the growth of vegan-friendly establishments worldwide. It has become a dynamic and inclusive culture that embraces diversity and encourages positive change for animals, the environment, and personal well-being."
    ),
    Article(
        title = "History of Computer",
        contents = "The history of computers dates back to the early 19th century when mechanical devices like the Jacquard loom and the Analytical Engine laid the foundation for computing concepts." +
                "The invention of the electronic computer in the mid-20th century, such as the ENIAC, marked a significant breakthrough in computing technology." +
                "The development of transistors in the late 1940s and subsequent integrated circuits in the 1960s led to the miniaturization of computers and the birth of mainframe and minicomputer systems." +
                "The 1970s witnessed the emergence of personal computers (PCs) with the introduction of machines like the Altair 8800 and the Apple II, making computing more accessible to individuals." +
                "The 1980s and 1990s saw the rapid evolution of PCs, with advancements in processors, storage, graphical user interfaces (GUIs), and networking, leading to the widespread adoption of computers in homes and businesses." +
                "The 21st century brought further innovations in computing, including the rise of mobile devices, cloud computing, artificial intelligence, and the Internet of Things (IoT), transforming the way we interact with computers and expanding their capabilities in various domains."
    ),
    Article(
        title = "Advantages and disadvantages of SNS",
        contents = "Advantages :\n\n" +
                "1. Connectivity and Communication: SNS enable people to connect and communicate with friends, family, and acquaintances globally, fostering social connections and bridging geographical barriers.\n\n" +
                "2. Information Sharing: SNS allow users to share information, news, and ideas, facilitating the spread of knowledge and awareness on various topics.\n\n" +
                "3. Networking and Professional Opportunities: SNS provide platforms for networking, which can lead to professional opportunities, collaborations, and career advancements.\n\n" +
                "4. Community Building: SNS enable the formation of online communities around shared interests, hobbies, and causes, fostering a sense of belonging and support.\n\n" +
                "5. Promoting Creativity and Expression: SNS offer avenues for individuals to express themselves, showcase their talents, and engage in creative endeavors, such as writing, photography, and art.\n\n" +
                "Disadvantages :\n\n" +
                "1. Privacy Concerns: SNS raise privacy issues, as personal information and activities shared online can be accessed by unintended parties, potentially leading to identity theft, harassment, or other risks.\n\n" +
                "2. Online Bullying and Trolling: SNS can be a breeding ground for cyberbullying, hate speech, and trolling, creating negative and harmful online environments.\n\n" +
                "3. Addiction and Time Wasting: Excessive use of SNS can lead to addiction, affecting productivity, personal relationships, and overall well-being.\n\n" +
                "4. Spread of Misinformation: SNS can be a platform for the rapid dissemination of false or misleading information, leading to the spread of rumors, conspiracy theories, and the erosion of trust in traditional media.\n\n" +
                "5. Comparison and Negative Impact on Mental Health: Constant exposure to carefully curated online personas and lifestyles can lead to social comparison, low self-esteem, and negative mental health effects.\n\n" +
                "It is important to use SNS responsibly, be mindful of privacy settings, exercise digital literacy, and maintain a healthy balance between online and offline activities to harness the benefits while mitigating the drawbacks of social networking sites.\n\n"
    ),
    Article(
        title = "Importance of Water",
        contents = "1. Water is essential for all forms of life on Earth, serving as a fundamental building block for biological processes.\n\n" +
                "2. It plays a crucial role in maintaining bodily functions, such as regulating temperature, transporting nutrients, and removing waste in humans and animals.\n\n" +
                "3. Water is vital for agriculture and food production, as it is necessary for irrigation, livestock, and crop growth.\n\n" +
                "4. It is a vital component of ecosystems, providing habitats for various species and supporting biodiversity.\n\n" +
                "5. Water is a valuable resource for industries and energy production, including manufacturing, power generation, and resource extraction.\n\n" +
                "6. The availability and quality of water are directly linked to human health, sanitation, and overall socio-economic development, making it a critical factor in global sustainability efforts.\n\n"
    ),
)
