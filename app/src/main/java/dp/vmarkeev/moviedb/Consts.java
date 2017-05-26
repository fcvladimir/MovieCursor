package dp.vmarkeev.moviedb;

/**
 * Created by vmarkeev on 01.03.2017.
 */

public class Consts {

    // The minimum number of items remaining before we should loading more.
    public static final int VISIBLE_THRESHOLD = 5;

    public class NetworkConstant {
        public static final int RETRY_COUNT = 3;
    }

    public class IntentConstant {
        public static final String ID = "id";
    }

    public class MovieConstant {
        public static final int POPULAR = 1;
        public static final int TOP_RATED = 2;
        public static final int FAVORITES = 3;
    }
}
