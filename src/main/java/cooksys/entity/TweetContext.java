// package cooksys.entity;
//
// import java.util.Set;
//
// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
// import javax.persistence.Id;
// import javax.persistence.ManyToMany;
// import javax.persistence.ManyToOne;
//
// import com.fasterxml.jackson.annotation.JsonIgnore;
//
// @Entity
// public class TweetContext {
//
// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;
//
// @ManyToOne
// @JsonIgnore
// private TweetI repost;
//
// @ManyToMany(mappedBy = "following")
// @JsonIgnore
// private Set<User> followers;
//
// public Long getId() {
// return id;
// }
//
// public void setId(Long id) {
// this.id = id;
// }
// }
