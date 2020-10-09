import {render, screen} from '@testing-library/react'
import React from 'react'
import userEvent from '@testing-library/user-event'
import {act} from "@testing-library/react";
import AddOrEditItem from "../AddOrEditItem";

window.matchMedia = window.matchMedia || function () {
    return {
        matches: false,
        addListener: function () {
        },
        removeListener: function () {
        }
    };
};

test('renders add/edit item component', () => {
    const props = {
        currentItem: { id: 1, name: "Yanki" },
        modalVisible: true,
        closeModal: jest.fn(),
        getItems: jest.fn()
    }
    render(<AddOrEditItem {...props} />);

    const addEditInput = screen.getByPlaceholderText("item name");
    const saveButton = screen.getByText(/Save/);
    const cancelButton = screen.getByText(/Cancel/);

    expect(addEditInput).toBeInTheDocument();
    expect(addEditInput.value).toBe(props.currentItem.name);
    expect(saveButton).toBeInTheDocument();
    expect(cancelButton).toBeInTheDocument();
    expect(saveButton).toBeEnabled();
});

test('renders add/edit item component with no current item', () => {
    const props = {
        currentItem: {},
        modalVisible: true,
        closeModal: jest.fn(),
        getItems: jest.fn()
    }
    render(<AddOrEditItem {...props} />);

    const addEditInput = screen.getByPlaceholderText("item name");
    const saveButton = screen.getByText(/Save/);
    const cancelButton = screen.getByText(/Cancel/);

    expect(addEditInput).toBeInTheDocument();
    expect(addEditInput.value).toBe("");
    expect(saveButton).toBeInTheDocument();
    expect(cancelButton).toBeInTheDocument();
    expect(saveButton).toBeDisabled();

    act(() => {
        userEvent.type(addEditInput, "hello there");
    });

    expect(saveButton).toBeEnabled();
});
