import models.Comment;
import models.Post;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;


public class CommentsRelation extends UnitTest {
	
	@Before
	public void setup() {
		Fixtures.deleteDatabase();
	}
	
	@Test
	public void testCommentsRelation(){
		//create new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob").save();
		
		//create new post
		Post bobsPost = new Post(bob, "My Post", "Hello World").save();
		
		//post a first comment
		bobsPost.addComment("Jeff", "I agree buddy").save();
		bobsPost.addComment("Jack", "I object!").save();
		
		//count things
		assertEquals(1, User.count());
		assertEquals(1, Post.count());
		assertEquals(2, Comment.count());
		
		//retrieve bobs post
		bobsPost = Post.find("byAuthor", bob).first();
		assertNotNull(bobsPost);
		
		//navigate to comments
		assertEquals(2, bobsPost.comments.size());
		assertEquals("Jeff", bobsPost.comments.get(0).author);
		
		//delete the post
		bobsPost.delete();
		
		//check that all comments have been deleted
		assertEquals(0, bobsPost.count());
		assertEquals(1, User.count());
		assertEquals(0, Post.count());
		assertEquals(0, Comment.count());
	}

}
