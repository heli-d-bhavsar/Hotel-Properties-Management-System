package com.coder.hms.ui.extras;

public class TableRendererFactory extends AbstractFactory {

    @Override
    public TableRenderer getTableRenderer(String tableRendererType) {
        if(tableRendererType.equalsIgnoreCase("ALL RESERVATION")){
            return new AllreservationRenderer();
        }else if(tableRendererType.equalsIgnoreCase("AUDIT TABLE")){
            return new AuditTableCellRenderer();
        }
        else if(tableRendererType.equalsIgnoreCase("BLOCKADE TABLE CELL")){
            return new BlockadeTableCellRenderer();
        }
        else if(tableRendererType.equalsIgnoreCase("CUSTOMERS TABLE")){
            return new CustomersTableRenderer();
        }
        else if(tableRendererType.equalsIgnoreCase("PAYHOST TABLE")){
            return new PayPostTableCellRenderer();
        }
        else if(tableRendererType.equalsIgnoreCase("RESERVATION TABLE")){
            return new ReservationTableRenderer();
        }
        else if(tableRendererType.equalsIgnoreCase("ROOM CLEANING TABLE")){
            return new RoomCleaningTableRenderer();
        }
        return null;
    }
}
