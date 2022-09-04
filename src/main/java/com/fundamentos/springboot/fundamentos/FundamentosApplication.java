package com.fundamentos.springboot.fundamentos;

import com.fundamentos.springboot.fundamentos.bean.MyBean;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentos.springboot.fundamentos.component.ComponentDependency;
import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.pojo.UserPojo;
import com.fundamentos.springboot.fundamentos.repository.UserRepository;
import com.fundamentos.springboot.fundamentos.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);
	private ComponentDependency componentDependency;
	private MyBean myBean;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;
	private UserService usersService;

	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository, UserService usersService) {
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.usersService = usersService;
	}
	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ejemplosAnteriores();
		saveUsersInDataBase();
		getInformationJpqlFromUser();
		saveWithErrorTransactional();
	}

	private void saveWithErrorTransactional() {
		User test1 = new User("Test Transactional 1", "TestTransactional_1@domain.com", LocalDate.now());
		User test2 = new User("Test Transactional 2", "TestTransactional_2@domain.com", LocalDate.now());
		User test3 = new User("Test Transactional 3", "TestTransactional_1@domain.com", LocalDate.now());
		User test4 = new User("Test Transactional 4", "TestTransactional_4@domain.com", LocalDate.now());

		List<User> users = Arrays.asList(test1, test2, test3, test4);

		try{
			usersService.saveTransactional(users);
		} catch (Exception e) {
			LOGGER.error("Esta es una excepción dentro del método transaccional " + e);
		}

		usersService.getAllUsers().stream().forEach(user -> LOGGER.info("Este es el usuario dentro del método transaccional " + user));
	}

	private void getInformationJpqlFromUser() {

		LOGGER.info("Usuario con el método findByEmail: " +
				userRepository.findByEmail("julie@mail.com")
				.orElseThrow(() -> new RuntimeException("No se encontró el usuario")));

		userRepository.findAndSort("user", Sort.by("id").ascending())
				.stream()
				.forEach(user -> LOGGER.info("Usuario encontrado con método sort: " + user));

		userRepository.findByName("John")
				.stream()
				.forEach(user -> LOGGER.info("Usuario con query method: " + user));

		LOGGER.info("Usuario con query method findByEmailAndName: " + userRepository.findByEmailAndName("daniela@mail.com", "Daniela")
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado")));

		userRepository.findByNameLike("%J%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameLike: " + user));

		userRepository.findByNameOrEmail(null, "user10@mail.com")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameOrEmail: " + user));

		userRepository.findByNameOrEmail("user 4", null)
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameOrEmail: " + user));

		userRepository.findByBirthDateBetween(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 10, 2))
				.stream()
				.forEach(user -> LOGGER.info("Usuario con intervalo de fechas: " + user));

		userRepository.findByNameLikeOrderByIdDesc("%user%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario encontrado con like y ordenado: " + user));

		userRepository.findByNameContainingOrderByIdDesc("user")
				.stream()
				.forEach(user -> LOGGER.info("Usuario encontrado con containing y ordenado: " + user));

		LOGGER.info("El usuario a partir del named parameter es: " + userRepository.getAllByBirthDateAndEmail(LocalDate.of(2021,5,2), "daniela@mail.com")
				.orElseThrow(() -> new RuntimeException("No se encontró el usuario a partir del named parameter")));
	}

	private void saveUsersInDataBase() {
		User user1 = new User("John", "john@mail.com", LocalDate.of(2021,3,20));
		User user2 = new User("Julie", "julie@mail.com", LocalDate.of(2021,4,10));
		User user3 = new User("Daniela", "daniela@mail.com", LocalDate.of(2021,5,2));
		User user4 = new User("user 4", "user4@mail.com", LocalDate.of(2021,6,3));
		User user5 = new User("John", "user5@mail.com", LocalDate.of(2021,7,7));
		User user6 = new User("user 6", "user6@mail.com", LocalDate.of(2021,8,8));
		User user7 = new User("user 7", "user7@mail.com", LocalDate.of(2021,9,17));
		User user8 = new User("user 8", "user8@mail.com", LocalDate.of(2021,10,18));
		User user9 = new User("user 9", "user9@mail.com", LocalDate.of(2021,1,25));
		User user10 = new User("user 10", "user10@mail.com", LocalDate.of(2021,2,26));
		User user11 = new User("user 11", "user11@mail.com", LocalDate.of(2021,11,27));
		User user12 = new User("user 12", "user12@mail.com", LocalDate.of(2021,12,22));

		List<User> list = Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11, user12);
		list.stream().forEach(userRepository::save);
	}

	private void ejemplosAnteriores() {
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + " - " + userPojo.getPassword());
		try {
			// error
			int value = 10/0;
			LOGGER.debug("Mi valor es: " + value);
		} catch (Exception e) {
			LOGGER.error("Esto es un error al dividir por cero " + e.getMessage());
		}
	}
}
