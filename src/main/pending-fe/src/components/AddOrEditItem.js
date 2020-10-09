import React, {useState, useEffect} from 'react';
import {Modal, Button, Input} from 'antd';
import {queryApi} from "../helpers/ApiUtils";
import {PENDING_ITEMS_BASE_URL} from "../helpers/Constants";

function AddOrEditItem(props) {

    const [item, setItem] = useState({name: props.currentItem.name, id: props.currentItem.id});

    /**
     * used to update the state from the props
     */
    useEffect(() => {
        setItem({name: props.currentItem.name, id: props.currentItem.id});
    }, [props.currentItem])

    /**
     * handle save or update item
     */
    const handleOk = () => {
        let resp;
        if (item.id) {
            resp = queryApi(`${PENDING_ITEMS_BASE_URL}/${item.id}`, { "name": item.name }, "POST");
        } else {
            resp = queryApi(`${PENDING_ITEMS_BASE_URL}`, { "name": item.name }, "POST");
        }
        props.closeModal();
        // reload the lists after adding an item
        resp.then(res => props.getItems());
    };
    // disable save button if no name is set
    const enableSave = item.name != null && item.name.length > 0;

    return (
        <>
            <Modal
                title="Add/Edit Item"
                visible={props.modalVisible}
                onOk={handleOk}
                onCancel={props.closeModal}
                footer={[
                    <Button key="back" onClick={props.closeModal}>
                        Cancel
                    </Button>,
                    <Button key="submit" type="primary" onClick={handleOk} disabled={!enableSave}>
                        Save
                    </Button>,
                ]}
            >
                <div>
                    <Input placeholder="item name"
                           defaultValue={item.name}
                           value={item.name}
                           onChange={(e) => {
                               setItem({name: e.target.value, id: props.currentItem.id});
                           }}/>
                </div>
            </Modal>
        </>
    );
}

export default AddOrEditItem;
