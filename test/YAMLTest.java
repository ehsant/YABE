import java.util.List;

import models.Comment;
import models.Post;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class YAMLTest extends UnitTest {

	@Before
	public void setup() {
		Fixtures.deleteDatabase();
	}

	@Test
	public void fullTest() {
		Fixtures.loadModels("data.yml");

		// count things
		assertEquals(2, User.count());
		assertEquals(3, Post.count());
		assertEquals(3, Comment.count());

		// try to connect as a user
		assertNotNull(User.connect("bob@gmail.com", "secret"));
		assertNotNull(User.connect("jeff@gmail.com", "secret"));
		assertNull(User.connect("bob@gmail.com", "saywhat?"));
		assertNull(User.connect("tom@gmail.com", "secret"));

		// Find all of Bob's posts
		List<Post> bobPosts = Post.find("author.email", "bob@gmail.com")
				.fetch();
		assertEquals(2, bobPosts.size());

		// Find all comments related to Bob's posts
		List<Comment> bobComments = Comment.find("post.author.email",
				"bob@gmail.com").fetch();
		assertEquals(3, bobComments.size());

		// Find the most recent post
		Post mostRecentPost = Post.find("order by postedAt desc.").first();
		assertNotNull(mostRecentPost);
		assertEquals("About the model layer", mostRecentPost.title);

		// Check that this post has two comments
		assertEquals(2, mostRecentPost.comments.size());

		// Post a new comment
		mostRecentPost.addComment("Ehsan", "Whats up?");
		assertEquals(3, mostRecentPost.comments.size());
		assertEquals(4, Comment.count());

	}
}
