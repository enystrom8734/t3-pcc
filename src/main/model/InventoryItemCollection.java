// specify the package
package model;

// system imports
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import exception.MultiplePrimaryKeysException;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


//==============================================================
public class InventoryItemCollection  extends EntityBase implements IView
{
    private static final String myTableName = "Inventory";
    private Vector<ClothingItem> inventoryItems;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public InventoryItemCollection( )
    {
        super(myTableName);
    }

    //-----------------------------------------------------------
    private void populateCollectionHelper(String query)
    {
        Vector<Properties> allClothingItemsRetrieved = getSelectQueryResult(query);
        Vector<Properties> allColorItemsRetrieved = getColors();
//        System.out.println(allColorItemsRetrieved.toString());
        Vector<Properties> allArticleTypesRetrieved = getArticleTypes();
//        System.out.println(allArticleTypesRetrieved);
        Iterator colorIterator = null;
        Iterator articleTypeIterator = null;

//        System.out.println(allDataRetrieved.toString().replace("}", "\n"));
        if (allClothingItemsRetrieved != null)
        {
            inventoryItems = new Vector<ClothingItem>();

            for (int cnt = 0; cnt < allClothingItemsRetrieved.size(); cnt++)
            {
                Properties nextInventoryItemData = allClothingItemsRetrieved.elementAt(cnt);
                ClothingItem ci = new ClothingItem(nextInventoryItemData);
//                System.out.println(ci.getState("Barcode"));
                if (ci != null)
                {

                    String color1 = "";
                    String color2 = "";
                    String articleType = "";
                    Properties temp = null;
                    colorIterator = allColorItemsRetrieved.iterator();
                    articleTypeIterator = allArticleTypesRetrieved.iterator();

                    while(colorIterator.hasNext())
                    {
                     temp = (Properties) colorIterator.next();
//                     System.out.println(temp.toString());
                     if(((String)temp.getProperty("ID")).equals(ci.getState("Color1")))
                     {
                         color1 = temp.getProperty("Description");
//                       System.out.println(color1);
                     }
                     if(((String)temp.getProperty("ID")).equals(ci.getState("Color2")))
                     {
                         color2 = temp.getProperty("Description");
//                       System.out.println(color2);
                     }
                    }
                    while(articleTypeIterator.hasNext())
                    {
                        temp = (Properties) articleTypeIterator.next();
                        if(((String)temp.getProperty("ID")).equals(ci.getState("ArticleType")))
                        {
                            articleType = temp.getProperty("Description");
//                          System.out.println(articleType);
                        }

                    }
                    formatClothingItem(ci, color1, color2, articleType);
                    inventoryItems.add(ci);
                }
            }
        }
    }

    //-----------------------------------------------------------
   public void formatClothingItem(ClothingItem ci, String color1, String color2, String articleType)
   {
       if(ci.getState("ArticleType") != null)
       {
           ci.stateChangeRequest("ArticleType", articleType);
       }
       if(ci.getState("Color1") != null)
       {
           ci.stateChangeRequest("Color1", color1);
       }
       if(ci.getState("Color2") != null)
       {
           ci.stateChangeRequest("Color2", color2);
       }
   }

    //-----------------------------------------------------------
    public Vector<Properties> getColors()
    {
        String query = "Select Id, Description FROM color;";
        return getSelectQueryResult(query);
    }

    //-----------------------------------------------------------
    public Vector<Properties> getArticleTypes()
    {
        String query = "SELECT Id, Description FROM articletype;";
        return getSelectQueryResult(query);
    }

    //-----------------------------------------------------------
    public void findAll()
    {
        String query = "SELECT * FROM " + myTableName + " WHERE (Status = 'Donated')";
        populateCollectionHelper(query);
    }

    //-----------------------------------------------------------
    public String getColorDescription(String colorBarcodePrefix)
    {
        String query = "SELECT Description FROM `color` WHERE BarcodePrefix = " + colorBarcodePrefix;
        return getSelectQueryResult(query).toString();
    }

    //-----------------------------------------------------------
    public String getArticleTypeDescription(String articleTypeBarcodePrefix)
    {
        String query = "SELECT Description FROM `articletype` WHERE BarcodePrefix = " + articleTypeBarcodePrefix;
        return getSelectQueryResult(query).toString();
    }

    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("InventoryItems"))
            return inventoryItems;
        else
        if (key.equals("InventoryList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //------------------------------------------------------
    protected void createAndShowView()
    {

        Scene localScene = myViews.get("InventoryItemCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("InventoryItemCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("InventoryItemCollectionView", localScene);
        }
        // make the view visible by installing it into the frame
        swapToView(localScene);

    }

    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}