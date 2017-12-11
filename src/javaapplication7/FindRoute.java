package javaapplication7;


/****
 *This Method finds the best route based on user input (minimum elevation,
 * Maximum elevation or the shortest root(if not mentioned))
 */
public class FindRoute {

    public static float getScore(float g, float h, float e)
    {
        boolean minimize_elevation = true;
        float percentage = 100.0f;
        float f;
        float max_e = 100.0f;

        if(minimize_elevation)
        {
            f = g+h+(percentage*e);
        }
        else
        {
            float compliment_e = max_e - e;
            f = g+h+(percentage*compliment_e);
        }
        return f;
    }
}
