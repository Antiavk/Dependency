package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {

  private final Map<Long, Post> posts = new ConcurrentHashMap<>(){};
  private final AtomicLong counter = new AtomicLong(0L);

  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(posts.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0){
      post.setId(Long.valueOf(counter.incrementAndGet()));
      posts.put(post.getId(), post);
    }
    //id !=0, значит, это сохранение (обновление) существующего поста.
    else if (posts.containsKey(post.getId())){
      posts.put(post.getId(), post);
    } else {
      throw new NotFoundException("ID Not Found");
    }
    return post;
  }

  public void removeById(long id) {
    posts.remove(id);
  }
}