package porsche.coffeeKitchen;

import porsche.coffeeKitchen.consumer.Consumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:integrationTest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class IntegrationTest {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void testGetAll() {
		//given
		String[] names = {"Anton", "Bogger", "Caesar", "Dora", "Emil","Friedrich", "Gustav", "Heinrich", "Ida", "Julius", "Kaufmann", "Ludwig", "Martha", "Nusspli"};
		for (String name : names) {
			restTemplate.exchange("/api/v1/consumer/:" + name, HttpMethod.POST, null, Consumer.class);
		}

		//when
		Consumer[] testConsumers = restTemplate.getForObject("/api/v1/consumer", Consumer[].class);

		//then
		assertThat(testConsumers.length).isEqualTo(14);
		assertThat(testConsumers[13].toString()).isEqualTo("Consumer{id=14, name='Nusspli', count=0, credit=0.00}");
	}

	@Test
	void testGetConsumer() {
		//given
		restTemplate.exchange("/api/v1/consumer/:Anton", HttpMethod.POST, null, Consumer.class);

		//when
		Consumer response = restTemplate.getForObject("/api/v1/consumer/:Anton", Consumer.class);

		//then
		assertThat(response.toString()).isEqualTo("Consumer{id=1, name='Anton', count=0, credit=0.00}");
	}

	@Test
	void testGetConsumerUnknownConsumerException() {
		//given: empty db
		//when
		ResponseEntity<Consumer> getResponse= restTemplate.exchange("/api/v1/consumer/:Bogger", HttpMethod.GET, null,  Consumer.class);

		//then
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void testAddConsumer(){
		//given: empty db
		//when
		ResponseEntity<Consumer> postResponse= restTemplate.exchange("/api/v1/consumer/:Caesar", HttpMethod.POST, null, Consumer.class);
		Consumer getResponse = restTemplate.getForObject("/api/v1/consumer/:Caesar", Consumer.class);

		//then
		assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.toString()).isEqualTo("Consumer{id=1, name='Caesar', count=0, credit=0.00}");
	}

	@Test //how to check for Message?
	void testAddConsumerDuplicateConsumerException(){
		//given: empty db
		//when
		ResponseEntity<Consumer> postResponse1= restTemplate.exchange("/api/v1/consumer/:Dora", HttpMethod.POST, null, Consumer.class);
		ResponseEntity<Consumer> postResponse2= restTemplate.exchange("/api/v1/consumer/:Dora", HttpMethod.POST, null, Consumer.class);

		//then
		assertThat(postResponse1.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(postResponse2.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
	}

	@Test
	void testAddCredit() {
		//given:
		restTemplate.exchange("/api/v1/consumer/:Emil", HttpMethod.POST, null, Consumer.class);

		//when
		ResponseEntity<Consumer> putResponse =  restTemplate.exchange("/api/v1/consumer/:Emil/:5", HttpMethod.PUT, null, Consumer.class);
		Consumer getResponse = restTemplate.getForObject("/api/v1/consumer/:Emil", Consumer.class);

		//then
		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.toString()).isEqualTo("Consumer{id=1, name='Emil', count=0, credit=5.00}");
	}

	@Test
	void testAddCreditUnknownConsumerException() {
		//given: empty db
		//when
		ResponseEntity<Consumer> putResponse =  restTemplate.exchange("/api/v1/consumer/:Friedrich/:5", HttpMethod.PUT, null, Consumer.class);

		//then
		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}


	@Test
	void testDrinkCoffee() {
		//given
		restTemplate.exchange("/api/v1/consumer/:Gustav", HttpMethod.POST, null, Consumer.class);
		restTemplate.exchange("/api/v1/consumer/:Gustav/:8", HttpMethod.PUT, null, Consumer.class);
		//when
		for (int i = 0;i < 5; i++) {
			restTemplate.exchange("/api/v1/consumer/:Gustav", HttpMethod.PUT, null, Consumer.class);
		}
		Consumer getResponse = restTemplate.getForObject("/api/v1/consumer/:Gustav", Consumer.class);

		//then
		assertThat(getResponse.toString()).isEqualTo("Consumer{id=1, name='Gustav', count=5, credit=1.25}");
	}

	@Test
	void testDrinkCoffeeInsufficientCreditException() {
		//given
		restTemplate.exchange("/api/v1/consumer/:Heinrich", HttpMethod.POST, null, Consumer.class);

		//when
		ResponseEntity<Consumer> putResponse = restTemplate.exchange("/api/v1/consumer/:Heinrich", HttpMethod.PUT, null, Consumer.class);

		//then
		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.PAYMENT_REQUIRED);
	}

	@Test
	void testDrinkCoffeeUnknownConsumerException() {
		//given: empty db
		//when
		ResponseEntity<Consumer> putResponse = restTemplate.exchange("/api/v1/consumer/:Ida", HttpMethod.PUT, null, Consumer.class);

		//then
		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}