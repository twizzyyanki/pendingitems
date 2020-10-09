import React, {useState, useEffect} from 'react';
import {Layout, List, Button, Skeleton, Input, Spin} from 'antd';
import {BulbTwoTone, DeleteTwoTone, EditTwoTone, FileAddTwoTone} from '@ant-design/icons';
import {PENDING_ITEMS_BASE_URL} from '../helpers/Constants';
import {deleteApi, queryApi} from '../helpers/ApiUtils';
import AddOrEditItem from './AddOrEditItem';

const {Search} = Input;
const {Header, Content} = Layout;

function PendingItems() {

    // declare all needed state
    const [loading, setLoading] = useState(false);
    const [items, setItems] = useState([]);
    const [search, setSearch] = useState("");
    const [currentItem, setCurrentItem] = useState({});
    const [modalVisible, setModalVisible] = useState(false);

    /**
     * closes the modal box used for adding/editing items
     */
    const closeModal = () => {
        setModalVisible(false);
    }

    /**
     * opens the modal box used for adding/editing items
     */
    const openModal = () => {
        setModalVisible(true);
    }

    /**
     * function to handle editing an item
     * @param name - item name
     * @param id item id
     */
    const onEdit = (name, id) => {
        setCurrentItem({id, name});
        openModal();
    }

    /**
     * function to handle clicking the add item button
     */
    const onAdd = () => {
        setCurrentItem({});
        openModal();
    }

    /**
     * function to handle clicking the search button
     * @param search
     */
    const onSearch = search => {
        setSearch(search);
    }

    /**
     * get all the items
     */
    const getItems = () => {
        setLoading(true);
        const items = queryApi(`${PENDING_ITEMS_BASE_URL}/all`);
        items.then(d => {
            setItems(d);
            setLoading(false);
        }).finally(() => {
            setLoading(false);
        });
    }

    /**
     * function to handle delete button click
     * @param id
     */
    const onDelete = (id) => {
        deleteApi(`${PENDING_ITEMS_BASE_URL}/${id}`).then(() => {
            getItems();
        });
    }

    /**
     * used to call search api whenever search state changes
     */
    useEffect(() => {
        setLoading(true);
        const items = queryApi(`${PENDING_ITEMS_BASE_URL}?search=${search}`,);
        items.then(d => {
            setItems(d);
            setLoading(false);
        });
    }, [search]);

    /**
     * get all items
     */
    useEffect(() => {
        getItems();
    }, []);

    return (
        <Layout>
            <Header><h2>My Pending Items</h2></Header>
            <Content>
                <div className="todoListWrapper">
                    <div className="search">
                        <div className="add-item">
                            <Button onClick={onAdd} type="primary" icon={<FileAddTwoTone/>}>Add Item</Button>
                        </div>
                        <Search placeholder="search by name contains" onSearch={onSearch}/>
                    </div>
                    <div className="list-items">
                        {!loading ? <List
                            loading={loading}
                            itemLayout="horizontal"
                            dataSource={items}
                            renderItem={item => (
                                <List.Item
                                    actions={[
                                        <a title="Edit Item" key="list-loadmore-edit">{<EditTwoTone
                                            onClick={() => onEdit(item.name, item.id)}/>}</a>,
                                        <a title="Delete Item" key="list-loadmore-more">{<DeleteTwoTone onClick={() => onDelete(item.id)}
                                                                                    twoToneColor="#d62745"/>}</a>
                                    ]}
                                >
                                    <Skeleton avatar title={false} loading={item.loading} active>
                                        <List.Item.Meta
                                            avatar={
                                                <BulbTwoTone twoToneColor="green"/>
                                            }
                                            title={<span>{item.name}</span>}
                                        />
                                    </Skeleton>
                                </List.Item>
                            )}
                        /> : <Spin size="large"/>}
                    </div>
                    <AddOrEditItem {...{currentItem, modalVisible, closeModal, getItems}} />
                </div>
            </Content>
        </Layout>

    );
}

export default PendingItems;
