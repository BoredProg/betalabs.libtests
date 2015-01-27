/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package betalabs.libtests.temboo;

import com.temboo.Library.Delicious.GetTags;
import com.temboo.Library.Delicious.GetTags.GetTagsInputSet;
import com.temboo.Library.Delicious.GetTags.GetTagsResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

/**
 *
 * @author SEB__ALIEN
 */
public class ChoreosTests
{

    /**
     * Does not seem to be working with any Choreo. Message sent to support on 
     * 2015/01/27. Getting a "com.temboo.core.TembooHttpException" each time.
     * @throws TembooException 
     */
    void testChoreo_Delicious_GetTags() throws TembooException 
    {
        // Instantiate TembooSession object..
        TembooSession session = new TembooSession("capturevision", "myFirstApp", "bc155faadd9d41f6b7572d257a38e774");

        // Instantiate the Choreo, using the previously instantiated TembooSession object..
        GetTags getTagsChoreo = new GetTags(session);

        // Get an InputSet object for the choreo
        GetTagsInputSet getTagsInputs = getTagsChoreo.newInputSet();

        // Set inputs userName and password
        getTagsInputs.set_Password("xxx");
        getTagsInputs.set_Username("xxx");

        // Execute Choreo
        GetTagsResultSet getTagsResults = getTagsChoreo.execute(getTagsInputs);
        
        // print response..
        System.out.println(getTagsResults.get_Response());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TembooException
    {
        // TODO code application logic here
        new ChoreosTests().testChoreo_Delicious_GetTags();
    }

}
