package org.sonatype.nexus.vui;

import java.util.Comparator;

public class FeatureComparator
    implements Comparator<Feature>
{
    public int compare( Feature o1, Feature o2 )
    {
        int result = 0;

        // if both have orderRank set, compare it (if equal, "fallback" to name comparison)
        // if one of them does not have order rank, the one without is "less"
        // if none of them have order rank, compare by name
        if ( o1.getOrderRank() != Feature.ORDER_RANK_UNDEFINED && o2.getOrderRank() != Feature.ORDER_RANK_UNDEFINED )
        {
            result = o1.getOrderRank() - o2.getOrderRank();
        }
        else if ( o1.getOrderRank() == Feature.ORDER_RANK_UNDEFINED
            && o2.getOrderRank() != Feature.ORDER_RANK_UNDEFINED )
        {
            return 1;
        }
        else if ( o1.getOrderRank() != Feature.ORDER_RANK_UNDEFINED
            && o2.getOrderRank() == Feature.ORDER_RANK_UNDEFINED )
        {
            return -1;
        }

        if ( result == 0 )
        {
            result = o1.getName().compareTo( o2.getName() );
        }

        return result;
    }
}
