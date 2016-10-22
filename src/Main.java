import java.util.List;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserList;

public class Main {
	private static final String ownerScreenName = "kar_sazanami";
	private static final String listName = "柚p";
	
	public static void main(String[] args) {
		long listId = -1;
		TwitterFactory twitterFactory = new TwitterFactory();
		Twitter twitter = twitterFactory.getInstance();
		
		try{
			//ListIdを取得
			List<UserList> userLists = twitter.getUserLists(ownerScreenName);
			for(UserList v : userLists){
				if(("@" + ownerScreenName + "/" + listName).equals(v.getFullName())){
					listId = v.getId();
				}
				System.out.println(v.getFullName() + ", " + String.valueOf(v.getId()));
			}
			
			//リストが見つからなかったとき
			if(listId == -1){
				System.err.println("リストが見つかりませんでした．");
				System.exit(-1);
			}
			
			//リストを作成
			UserList createdList = twitter.createUserList(listName, true, "copied list");
			
			//ユーザー一覧
			PagableResponseList<User> users = twitter.getUserListMembers(listId, 5000, -1);
			long userIds[] = new long[users.size()];
			int i = 0;
			for(User v : users){
				System.out.println(v.getScreenName());
				//鍵垢じゃなければリストにメンバー追加
				//twitter.createUserListMember(listId, v.getId());
				userIds[i] = v.getId();
				i++;
			}
			//リストにメンバーを追加
			twitter.createUserListMembers(createdList.getId(), userIds);
			
			
		}catch(TwitterException te) {
			te.printStackTrace();
			System.exit(-1);
		}
		System.out.println("Finished!!");
	}
}
