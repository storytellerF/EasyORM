import com.storyteller_f.easyorm_search.SearchPattern;
import com.storyteller_f.sql_query.query.expression.LikeExpression;
import com.storyteller_f.sql_query.query.query.ExpressionQuery;

import java.util.ArrayList;
import java.util.Arrays;

public class UserSearch extends SearchPattern {
    private String name;
    private Integer age;

    public static String name() {
        return "name";
    }

    @Override
    public ExpressionQuery[] parse() {
        ArrayList<ExpressionQuery> expressionQueries = new ArrayList<>(1);
        if (stringNotEmpty(name)) {
            expressionQueries.add(new LikeExpression<>(UserSearch::name, name));
        }
        if (age !=null) {
            expressionQueries.add(new LikeExpression<>("age", age));
        }
        ExpressionQuery[] array=new ExpressionQuery[expressionQueries.size()];
        return expressionQueries.toArray(array);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
