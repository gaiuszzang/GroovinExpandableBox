package io.groovin.expandablebox.sampleapp.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import io.groovin.expandablebox.sampleapp.R

@Composable
fun MapBottomSheetScreen(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) {
    val data = remember { DescriptionOfKorea }
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .then(modifier)
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            fontSize = 18.sp,
            color = Color.Black,
            text = data.title
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            fontSize = 14.sp,
            color = Color.Gray,
            text = data.subTitle
        )
        Spacer(modifier = Modifier.height(12.dp))
        ImageRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            imageList = data.imageResourceIdList
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            fontSize = 14.sp,
            color = Color.Black,
            lineHeight = 1.6.em,
            fontWeight = FontWeight.Light,
            text = data.description
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun ImageRow(
    imageList: List<Int>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .then(modifier)
    ) {
        imageList.forEachIndexed { index, imageResourceId ->
            if (index != 0) Spacer(modifier = Modifier.width(12.dp))
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                painter = painterResource(id = imageResourceId),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}

private data class MapBottomSheetContent(
    val title: String,
    val subTitle: String,
    val imageResourceIdList: List<Int>,
    val description: String
)

private val DescriptionOfKorea = MapBottomSheetContent(
    title = "South Korea",
    subTitle = "대한민국",
    imageResourceIdList = listOf(
        R.drawable.map_sample_rok_01,
        R.drawable.map_sample_rok_02,
        R.drawable.map_sample_rok_03,
        R.drawable.map_sample_rok_04,
        R.drawable.map_sample_rok_05,
    ),
    description = "South Korea, officially the Republic of Korea (ROK), is a country in East Asia. " +
            "It constitutes the southern part of the Korean Peninsula and borders North Korea along the Korean Demilitarized Zone. " +
            "The country's western border is formed by the Yellow Sea, while its eastern border is defined by the Sea of Japan. " +
            "South Korea claims to be the sole legitimate government of the entire peninsula and adjacent islands. " +
            "It has a population of 51.96 million, of which roughly half live in the Seoul Capital Area, " +
            "the fourth most populous metropolitan area in the world. " +
            "Other major cities include Incheon, Busan, and Daegu.\n\n" +
            "The Korean Peninsula was inhabited as early as the Lower Paleolithic period. " +
            "Its first kingdom was noted in Chinese records in the early 7th century BCE. " +
            "Following the unification of the Three Kingdoms of Korea into Silla and Balhae in the late 7th century, " +
            "Korea was ruled by the Goryeo dynasty (918–1392) and the Joseon dynasty (1392–1897). " +
            "The succeeding Korean Empire (1897–1910) was annexed in 1910 into the Empire of Japan. " +
            "Japanese rule ended following Japan's surrender in World War II, " +
            "after which Korea was divided into two zones: a northern zone occupied by the Soviet Union, " +
            "and a southern zone occupied by the United States. After negotiations on reunification failed, " +
            "the southern zone became the Republic of Korea in August 1948, " +
            "while the northern zone became the communist Democratic People's Republic of Korea the following month.\n\n" +
            "In 1950, a North Korean invasion began the Korean War, which ended in 1953 after extensive fighting involving the " +
            "American-led United Nations Command and the People's Volunteer Army from China with Soviet assistance. " +
            "The war left 3 million Koreans dead and the economy in ruins. " +
            "The authoritarian First Republic of Korea led by Syngman Rhee was overthrown in the April Revolution of 1960. " +
            "However, the Second Republic was incompetent as it could not control the revolutionary fervor. " +
            "The May 16 coup of 1961 led by Park Chung Hee put an end to the Second Republic, " +
            "signaling the start of the Third Republic in 1963. " +
            "South Korea's devastated economy began to soar under Park's leadership, " +
            "recording the fastest rise in average GDP per capita. Despite lacking natural resources, " +
            "the nation rapidly developed to become one of the Four Asian Tigers " +
            "based on international trade and economic globalization, " +
            "integrating itself within the world economy with export-oriented industrialization. " +
            "The Fourth Republic was established after the October Restoration of 1972, in which Park wielded absolute power. " +
            "The Yushin Constitution declared that the president could suspend basic human rights and appoint a third of the parliament. " +
            "Suppression of the opposition and human rights abuse by the government became more severe in this period. " +
            "Even after Park's assassination in 1979, the authoritarian rule continued in the Fifth Republic led by Chun Doo-hwan, " +
            "which violently seized power by two coups and brutally suppressing the Gwangju Uprising. " +
            "The June Democratic Struggle of 1987 ended authoritarian rule, forming the current Sixth Republic. " +
            "The country is now considered among the most advanced democracies in Continental and East Asia.\n\n" +
            "South Korea maintains a unitary presidential republic under the 1987 constitution with " +
            "a unicameral legislature, the National Assembly. It is considered a regional power and a developed country, " +
            "with its economy ranked as the world's thirteenth-largest by nominal GDP and the fourteenth-largest by GDP (PPP). " +
            "Its citizens enjoy one of the world's fastest Internet connection speeds and densest high-speed railway networks. " +
            "The country is the world's ninth-largest exporter and ninth-largest importer. " +
            "Its armed forces are ranked as one of the world's strongest militaries, " +
            "with the world's second-largest standing army by military and paramilitary personnel. " +
            "In the 21st century, South Korea has been renowned for its globally influential pop culture, " +
            "particularly in music, TV dramas, and cinema, a phenomenon referred to as the Korean Wave. " +
            "It is a member of the OECD's Development Assistance Committee, the G20, the IPEF, and the Paris Club."
)