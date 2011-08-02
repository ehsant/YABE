import java.util.List;

import models.Comment;
import models.Post;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class BasicTest extends UnitTest {

	@Before
	public void setup() {
		Fixtures.deleteDatabase();
	}

	@Test
	public void createAndRetrieveUser() {
		// create a new user
		new User("bob@gmail.com", "secret", "Bob").save();

		// retrieve existing user by email address
		User bob = User.find("byEmail", "bob@gmail.com").first();

		// test
		assertNotNull(bob);
		assertEquals("Bob", bob.fullname);
		assertEquals("bob@gmail.com", bob.email);
		assertEquals("secret", bob.password);

	}

	@Test
	public void tryConnectAsUser() {

		// create new user and save it
		new User("bob@gmail.com", "secret", "Bob").save();

		assertNotNull(User.connect("bob@gmail.com", "secret"));
		assertNull(User.connect("bob@gmail.com", "badPassword"));
		assertNull(User.connect("tom@gmail.com", "secret"));

	}

	@Test
	public void testPost() {

		// create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob").save();

		// create new post and save it
		new Post(bob, "my first post", "Hello world").save();

		// test that the test post has been created
		assertEquals(1, Post.count());

		// retrieve all posts created by bob
		List<Post> bobPosts = Post.find("byAuthor", bob).fetch();

		// Tests
		assertEquals(1, bobPosts.size());
		Post firstPost = bobPosts.get(0);
		assertNotNull(firstPost);
		assertEquals(bob, firstPost.author);
		assertEquals("my first post", firstPost.title);
		assertEquals("Hello world", firstPost.content);
		assertNotNull(firstPost.postedAt);

	}

	@Test
	public void postComments() {
		// create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob").save();

		// create a new post
		Post bobPost = new Post(bob, "bobs post", "Hello World2").save();

		// post first comment
		new Comment(bobPost, "Jeff", "cool post dude").save();
		new Comment(bobPost, "Jack", "This is the second comment dude!").save();

		// retrieve all comments
		List<Comment> bobPostComments = Comment.find("byPost", bobPost).fetch();

		// Tests
		assertEquals(2, bobPostComments.size());

		Comment firstComment = bobPostComments.get(0);
		assertNotNull(firstComment);
		assertEquals("Jeff", firstComment.author);
		assertEquals("cool post dude", firstComment.content);
		assertNotNull(firstComment.postedAt);

		Comment secondComment = bobPostComments.get(1);
		assertNotNull(secondComment);
		assertEquals("Jack", secondComment.author);
		assertEquals("This is the second comment dude!", secondComment.content);
		assertNotNull(secondComment.postedAt);

	}

}
