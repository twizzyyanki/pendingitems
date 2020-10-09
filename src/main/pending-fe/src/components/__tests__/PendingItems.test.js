import {render, screen} from '@testing-library/react'
import React from 'react'
import userEvent from '@testing-library/user-event'
import {act} from "@testing-library/react";
import PendingItems from "../PendingItems";

window.matchMedia = window.matchMedia || function () {
    return {
        matches: false,
        addListener: function () {
        },
        removeListener: function () {
        }
    };
};

test('renders pending item component', () => {
    render(<PendingItems/>);
    const addItem = screen.getByText(/Add Item/i);
    const searchInput = screen.getByPlaceholderText("search by name contains");
    const header = screen.getByText(/My Pending Items/);

    expect(addItem).toBeInTheDocument();
    expect(searchInput).toBeInTheDocument();
    expect(header).toBeInTheDocument();
});

test('it opens add/edit modal', () => {
    render(<PendingItems/>);
    const addItem = screen.getByText(/Add Item/i);

    act(() => {
        userEvent.click(addItem);
    });

    const addEditInput = screen.getByPlaceholderText("item name");
    const saveButton = screen.getByText(/Save/);
    const cancelButton = screen.getByText(/Cancel/);

    expect(addEditInput).toBeInTheDocument();
    expect(saveButton).toBeInTheDocument();
    expect(cancelButton).toBeInTheDocument();
    expect(saveButton).toBeDisabled();
});
